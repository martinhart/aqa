/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class InterpreterExceptionTest {
    
    private InterpreterException subject;
    
    @Test
    public void testGetLine() {
        subject = new InterpreterException(23, "hello");
        assertEquals(23, subject.getLine());
        
        subject = new InterpreterException("eh");
        assertEquals(1, subject.getLine());
    }
    
}
