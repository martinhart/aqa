/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.value.IntegerValue;
import aqa.value.Value;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class IntegerValueTest {
    
    private IntegerValue subject;
    
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
    
}
