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
public class ValueBaseTest {
    
    private ValueBase subject;
    
    @Before
    public void setup() {
        subject = new ValueBase("test");
    }
    
    @Test(expected=InterpreterException.class)
    public void testBugout() throws InterpreterException {
        subject.bugout("hmm");
    }
    
    @Test(expected=InterpreterException.class)
    public void testOutput() throws InterpreterException {
        subject.output();
    }
    
    @Test(expected=InterpreterException.class)
    public void testCompare() throws InterpreterException {
        subject.compare(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testEqual() throws InterpreterException {
        subject.equal(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testAdd() throws InterpreterException {
        subject.add(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testSubtract() throws InterpreterException {
        subject.subtract(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testMultiply() throws InterpreterException {
        subject.multiply(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testDivide() throws InterpreterException {
        subject.divide(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testDiv() throws InterpreterException {
        subject.div(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testMod() throws InterpreterException {
        subject.mod(new ValueBase("other"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testLength() throws InterpreterException {
        subject.length();
    }
    
    @Test
    public void testGetValue() {
        assertNull(subject.getValue());
    }
    
    @Test(expected=InterpreterException.class)
    public void testToStr() throws InterpreterException {
        subject.toStr();
    }
    
    @Test(expected=InterpreterException.class)
    public void testSetAtIndex() throws InterpreterException {
        subject.setAtIndex(0, new ValueBase(""));
    }
    
    @Test(expected=InterpreterException.class)
    public void testGetAtIndex() throws InterpreterException {
        subject.getAtIndex(0);
    }
    
    @Test
    public void testMakeCopy() throws InterpreterException {
        assertEquals(subject, subject.makeCopy());
    }
}
