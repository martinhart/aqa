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
public class IntegerValueTest {
    
    private IntegerValue subject;
    
    @Before
    public void setup() {
        subject = new IntegerValue(0);
    }
    
    @Test
    public void testInspect() {
        subject = new IntegerValue(1);
        assertEquals("1", subject.inspect());
    }
    
    @Test
    public void testOutput() {
        subject = new IntegerValue(10);
        assertEquals("10", subject.output());
    }
    
    @Test
    public void testCompareEqualWithIntegerValue() throws Exception {
        IntegerValue other = new IntegerValue(1);
        subject = new IntegerValue(1);
        assertEquals(0, subject.compare(other));
    }
    
    @Test
    public void testCompareLessWithIntegerValue() throws Exception {
        IntegerValue other = new IntegerValue(10);
        subject = new IntegerValue(9);
        assertEquals(-1, subject.compare(other));
    }
    
    @Test
    public void testCompareGreaterWithIntegerValue() throws Exception {
        IntegerValue other = new IntegerValue(123);
        subject = new IntegerValue(124);
        assertEquals(1, subject.compare(other));
        
        Value o = new IntegerValue(1000000);
        assertEquals(-1, subject.compare(o));
    }
    
    @Test(expected=InterpreterException.class)
    public void testCompareWithNonInteger() throws Exception {
        subject.compare(new StringValue("woo"));
    }
    
    @Test
    public void testEqualWhenEqual() throws Exception {
        Value other = new IntegerValue(1);
        subject = new IntegerValue(1);
        assertEquals(true, subject.equal(other));
    }
    
    @Test
    public void testEqualWhenNotEqual() throws Exception {
        Value other = new IntegerValue(2);
        subject = new IntegerValue(1);
        assertEquals(false, subject.equal(other));
    }
    
    @Test(expected=InterpreterException.class)
    public void testEqualWithNonInteger() throws Exception {
        subject.equal(new StringValue(""));
    }
    
    @Test(expected=InterpreterException.class)
    public void testAddWithNonInteger() throws Exception {
        subject.add(new StringValue(""));
    }
    
    @Test(expected=InterpreterException.class)
    public void testSubtractWithNonInteger() throws Exception {
        subject.subtract(new StringValue(""));
    }
    
    @Test(expected=InterpreterException.class)
    public void testMultiplyWithNonInteger() throws Exception {
        subject.multiply(new StringValue(""));
    }
    
    @Test(expected=InterpreterException.class)
    public void testDivideWithNonInteger() throws Exception {
        subject.divide(new StringValue(""));
    }
    
    @Test(expected=InterpreterException.class)
    public void testDivWithNonInteger() throws Exception {
        subject.div(new StringValue(""));
    }
    
    @Test(expected=InterpreterException.class)
    public void testModWithNonInteger() throws Exception {
        subject.mod(new StringValue(""));
    }
    
    @Test
    public void testToStr() throws Exception {
        assertEquals(true, new StringValue("0").equal(subject.toStr()));
    }
}
