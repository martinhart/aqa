/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.value.BooleanValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class BooleanValueTest {
     
    private BooleanValue subject;
    
    @Test
    public void testInspect() {
        subject = new BooleanValue(true);
        assertEquals("TRUE", subject.inspect());
    }
    
    @Test
    public void testOutput() {
        subject = new BooleanValue(false);
        assertEquals("FALSE", subject.output());
    }
    
}
