/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class ValueStackImplTest {
    
    private ValueStackImpl subject;
    
    @Before
    public void setUp() {
        subject = new ValueStackImpl();
    }

    @Test
    public void testSomeMethod() {
    }

    @Test
    public void testPushValue() throws InterpreterException {
        Value v = new ValueStub();
        subject.push(v);
        assertEquals(v, subject.pop());
    }
    
    @Test(expected=InterpreterException.class)
    public void testPopBooleanWithoutBoolean() throws InterpreterException {
        subject.push(new ValueStub());
        subject.popBoolean();
    }
    
    @Test
    public void testPopBoolean() throws InterpreterException {
        subject.push(true);
        assertEquals(true, subject.popBoolean());
        subject.push(false);
        assertEquals(false, subject.popBoolean());
    }
    
    @Test(expected=InterpreterException.class)
    public void testPopIntegerWithoutInteger() throws InterpreterException {
        subject.push(new ValueStub());
        subject.popInteger();
    }
    
    @Test
    public void testPopInteger() throws InterpreterException {
        subject.push(9);
        assertEquals(9, subject.popInteger());
    }
    
    @Test(expected=InterpreterException.class)
    public void testPopStringWithoutString() throws InterpreterException {
        subject.push(new ValueStub());
        subject.popString();
    }
    
    @Test
    public void testPopString() throws InterpreterException {
        subject.push("a");
        assertEquals("a", subject.popString());
    }
    
    private class ValueStub extends ValueBase {
        
        public ValueStub() {
            super("STUB");
        }
        
    }    
}
