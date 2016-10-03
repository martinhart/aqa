/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.tokenizer.Token;
import aqa.value.ArrayValue;
import aqa.value.StringValue;
import aqa.variable.Variable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class ParserVariablesTest {

    private Parser subject;
    private Variable variable;

    @Before
    public void setup() {
        subject = new Parser();
    }

    @Test
    public void testAssigningToAVariable() {
        addToken("a");
        addToken("<");
        addToken("-");
        addToken("34");
        parse();

        variable = subject.getVM().getVariable("a");
        assertEquals(34, variable.getValue().getValue());
        assertEquals(false, variable.isConstant());
    }

    @Test
    public void testCreatingAConstantVariable() {
        addTokens("constant", "a", "<", "-", "23");
        parse();

        variable = subject.getVM().getVariable("a");
        assertEquals(23, variable.getValue().getValue());
        assertEquals(true, variable.isConstant());
    }

    @Test
    public void testTryingToChangeAConstantVariable() {
        addTokens("constant", "a", "<", "-", "9", "a", "<", "-", "19");
        parse();
        variable = subject.getVM().getVariable("a");
        // in the real world an exception will be raised but parse() swallows it
        assertEquals(9, variable.getValue().getValue());
    }

    @Test
    public void testAssigningToA1DArray() throws InterpreterException {
        variable = new Variable("a");
        variable.setValue(new ArrayValue());
        subject.getVM().addVariable(variable);

        addToken("a");
        addToken("[");
        addToken("0");
        addToken("]");
        addToken("<");
        addToken("-");
        addToken("'Hi'");
        parse();

        ArrayValue av = (ArrayValue) variable.getValue();
        assertEquals("Hi", av.getAtIndex(0).getValue());
    }

    @Test
    public void testAssigningToA2DArray() throws InterpreterException {
        ArrayValue parent = new ArrayValue();
        ArrayValue child = new ArrayValue();
        variable = new Variable("a");
        parent.setAtIndex(0, child);
        variable.setValue(parent);
        subject.getVM().addVariable(variable);

        addTokens("a", "[", "0", "]", "[", "0", "]", "<", "-", "999");
        parse();

        assertEquals(parent, variable.getValue());
        assertEquals(999, child.getAtIndex(0).getValue());
    }

    @Test
    public void testTryingToArrayIndexANonArray() throws InterpreterException {
        // In the real world this will raise an exception.  Here we just
        // check that the variable is not changed.
        variable = new Variable("a");
        variable.setValue(new StringValue("woo"));
        subject.getVM().addVariable(variable);
        addTokens("a", "[", "0", "]", "<", "-", "10");
        parse();
        assertEquals("woo", variable.getValue().getValue());
    }

    private void addToken(String name) {
        try {
            subject.addToken(new Token(name, 1));
        } catch (InterpreterException ex) {
            Logger.getLogger(ParserVariablesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addTokens(String... tokens) {
        for (String token : tokens) {
            addToken(token);
        }
    }

    private void parse() {
        try {
            subject.parse();
        } catch (InterpreterException ex) {
            Logger.getLogger(ParserVariablesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
