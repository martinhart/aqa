/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.tokenizer.Token;
import aqa.value.ArrayValue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class ParserLiteralsTest {

    private Parser subject;

    @Before
    public void setup() {
        subject = new Parser();
    }

    @Test
    public void integerLiteral() throws InterpreterException {
        addToken("1");
        parse();
        assertEquals(1, subject.getVM().getValueStack().popInteger());
    }

    @Test
    public void negativeIntegerLiteral() throws InterpreterException {
        addToken("-123");
        parse();
        assertEquals(-123, subject.getVM().getValueStack().popInteger());
    }

    @Test
    public void realLiteral() throws InterpreterException {
        addToken("0.1");
        parse();
        assertEquals(0.1, subject.getVM().getValueStack().popReal(), 0);
    }

    @Test
    public void negativeRealLiteral() throws InterpreterException {
        addToken("-123.111");
        parse();
        assertEquals(-123.111, subject.getVM().getValueStack().popReal(), 0);
    }

    @Test
    public void trueLiteral() throws InterpreterException {
        addToken("TRUE");
        parse();
        assertEquals(true, subject.getVM().getValueStack().popBoolean());
    }

    @Test
    public void falseLiteral() throws InterpreterException {
        addToken("FALSE");
        parse();
        assertEquals(false, subject.getVM().getValueStack().popBoolean());
    }

    @Test
    public void stringLiteral() throws InterpreterException {
        addToken("'Help'");
        parse();
        assertEquals("Help", subject.getVM().getValueStack().popString());
    }

    @Test
    public void emptyArrayLiteral() throws InterpreterException, InterpreterException {
        addToken("[");
        addToken("]");
        parse();
        assertEquals(true, subject.getVM().peekValue() instanceof ArrayValue);
    }

    @Test
    public void fullArrayLiteral() throws InterpreterException, InterpreterException {
        ArrayValue ary;

        addToken("[");
        addToken("1");
        addToken(",");
        addToken("'2'");
        addToken("]");
        parse();

        ary = (ArrayValue) subject.getVM().popValue();
        assertEquals(2, ary.length().getValue());
        assertEquals(1, ary.getAtIndex(0).getValue());
        assertEquals("2", ary.getAtIndex(1).getValue());
    }

    private void addToken(String name) {
        try {
            subject.addToken(new Token(name, 1));
        } catch (InterpreterException ex) {
            Logger.getLogger(ParserLiteralsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parse() {
        try {
            subject.parse();
        } catch (InterpreterException ex) {
            Logger.getLogger(ParserLiteralsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
