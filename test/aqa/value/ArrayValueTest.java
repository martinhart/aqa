/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class ArrayValueTest {
    
    private ArrayValue subject;
    
    @Before
    public void setUp() {
        subject = new ArrayValue();
    }
    
    @Test
    public void length0WhenCreated() throws InterpreterException {
        assertEquals(0, subject.length().value);
    }
    
    @Test
    public void length1When1ItemAdded() throws InterpreterException {
        IntegerValue item = new IntegerValue(100);
        subject.setAtIndex(0, item);
        assertEquals(1, subject.length().value);
    }
    
    @Test
    public void length1GreaterThanHighestIndexUsed() throws InterpreterException {
        IntegerValue item = new IntegerValue(123);
        subject.setAtIndex(99, item);
        assertEquals(100, subject.length().value);
    }
    
    @Test
    public void setAndGetAtIndex() throws InterpreterException {
        IntegerValue i1 = new IntegerValue(1), i2 = new IntegerValue(2);
        subject.setAtIndex(0, i1);
        subject.setAtIndex(9, i2);
        assertEquals(10, subject.length().value);
        assertEquals(1, ((IntegerValue)subject.getAtIndex(0)).value);
        assertEquals(2, ((IntegerValue)subject.getAtIndex(9)).value);
        assertEquals(NullValue.instance, subject.getAtIndex(1));
    }
        
}
