/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.subroutine.Subroutine;
import aqa.tokenizer.Token;
import aqa.tokenizer.Tokenizer;
import aqa.tokenizer.Tokens;
import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class ParserSubroutinesTest {

    Parser subject;
    OutputWriterStub outputWriter;

    @Before
    public void setUp() {
        outputWriter = new OutputWriterStub();
        subject = new Parser(outputWriter, new NullInputProvider());
    }

    @Test
    public void initiallyHasNoTokens() {
        assertEquals(true, subject.tokens.isEmpty());
    }

    @Test
    public void testCreatingABasicEmptySubroutine() throws InterpreterException {
        subject.addToken(new Token("SUBROUTINE", 1));
        subject.addToken(new Token("name", 1));
        subject.addToken(new Token("(", 1));
        subject.addToken(new Token(")", 1));
        subject.addToken(new Token("ENDSUBROUTINE", 2));
        subject.parse();
        assertNotNull(subject.getVM().getSubroutine("name"));
    }

    @Test
    public void testCreatingASubroutineWithStatements() throws InterpreterException {
        Subroutine s;

        subject.addToken(new Token("SUBROUTINE", 1));
        subject.addToken(new Token("name", 1));
        subject.addToken(new Token("(", 1));
        subject.addToken(new Token(")", 1));
        subject.addToken(new Token("wibble", 2));
        subject.addToken(new Token("ENDSUBROUTINE", 3));
        subject.parse();

        s = subject.getVM().getSubroutine("name");
        assertNotNull(s);
        assertEquals(1, s.getTokens().size());
    }

    @Test
    public void testCreatingASubroutineWithParameters() throws InterpreterException {
        Subroutine s;

        subject.addToken(new Token("SUBROUTINE", 1));
        subject.addToken(new Token("name", 1));
        subject.addToken(new Token("(", 1));
        subject.addToken(new Token("param1", 1));
        subject.addToken(new Token(",", 1));
        subject.addToken(new Token("param2", 1));
        subject.addToken(new Token(")", 1));
        subject.addToken(new Token("wibble", 2));
        subject.addToken(new Token("ENDSUBROUTINE", 3));
        subject.parse();

        s = subject.getVM().getSubroutine("name");
        assertNotNull(s);
        assertEquals(2, s.getNumberOfParameters());
        assertEquals("param1", s.getParameter(0));
        assertEquals("param2", s.getParameter(1));
    }

    @Test
    public void testCallingASubroutineWithNoAruments() throws InterpreterException {
        addTokens("SUBROUTINE", "a", "(", ")", "OUTPUT",
                "1", "ENDSUBROUTINE", "a", "(", ")");
        subject.parse();
        assertEquals("1", outputWriter.output);
    }
    
    @Test
    public void testCallingASubroutineWithLiteralArgument() throws InterpreterException {
        addTokens("SUBROUTINE", "a", "(", "var", ")", "OUTPUT", "var",
                "ENDSUBROUTINE", "a", "(", "1", ")");
        subject.parse();
        assertEquals("1", outputWriter.output);
    }
    
    @Test
    public void testCallingASubroutineWithVariableArgument() throws InterpreterException {
        addTokens("SUBROUTINE", "a", "(", "var",
                ")", "OUTPUT", "var", "ENDSUBROUTINE", 
                "name", "<", "-", "'HELLO'", "a", "(", "name", ")");
        subject.parse();
        assertEquals("HELLO", outputWriter.output);
    }
    
    @Test
    public void testCallingASubroutineThatReturnsAValue() throws InterpreterException {
        addTokens("SUBROUTINE", "a", "(", ")", "RETURN", "'woo'",
                "ENDSUBROUTINE", "OUTPUT", "a", "(", ")");
        subject.parse();
        assertEquals("woo", outputWriter.output);
    }
    
    @Test
    public void testCallingASubroutineThatReturnsACalculation() throws InterpreterException {
        addTokens("SUBROUTINE", "add", "(", "a", ",", "b", ")",
                "RETURN", "a", "+", "b",
                "ENDSUBROUTINE", 
                "var", "<", "-", "add", "(", "1", ",", "2", ")",
                "var2", "<", "-", "add", "(", "3", ",", "4", ")",
                "OUTPUT", "var", "-", "var2");
        subject.parse();
        assertEquals("-4", outputWriter.output);
    }
    
    @Test
    public void testSubroutineArgumentsArePassedByValue() throws InterpreterException {
        String source = "SUBROUTINE func(item)\n" +
                "item <- 'CHANGED'\n" +
                "ENDSUBROUTINE\n" +
                "item <- 'original'\n" +
                "func(item)\n" +
                "OUTPUT item\n";
        tokenize(source);
        subject.parse();
        assertEquals("original", outputWriter.output);
    }
    
    @Test
    public void testArrayArgumentsArePassedByValue() throws InterpreterException {
        String source = "SUBROUTINE func(ary)\n" +
                "ary[0] <- 'CHANGED'\n" +
                "ENDSUBROUTINE\n" +
                "ary <- [1, 2]\n" +
                "func(ary)\n" +
                "OUTPUT ary\n";
        tokenize(source);
        subject.parse();
        assertEquals("12", outputWriter.output);
    }

    private void addTokens(String... tokens) throws InterpreterException {
        for (int i = 0; i < tokens.length; i++) {
            subject.addToken(new Token(tokens[i], i + 1));
        }
    }
    
    private void tokenize(String source) throws InterpreterException {
        Tokenizer t = new Tokenizer(new StringReader(source));
        Tokens toks = t.tokenize();
        for (Token tok : toks) {
            subject.addToken(tok);
        }
    }
    
    private class OutputWriterStub implements OutputWriter {

        public String output;

        public OutputWriterStub() {
            output = "";
        }
        
        @Override
        public void output(String message) {
            output += message;
        }
        
    }
}
