/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.value.Value;
import aqa.value.ValueBase;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class ArgumentTest {
    
    private Argument subject;
    
    @Test
    public void testConstructionWithValue() {
        Value v = new StubValue();
        subject = new Argument(v);
        assertEquals(v, subject.getValue());
    }
    
    private class StubValue extends ValueBase {
        
        public StubValue() {
            super("STUB");
        }
        
    }
}
