/*
 * Copyright (c) 2016 Martin Hart.  Under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.tokenizer.Token;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author martinhart
 */
public class TokenTest {
    
    @Test
    public void testLineNumber() throws Exception {
        Token t = new Token("hello", 2);
        assertEquals(2, t.getLineNumber());
        
        t = new Token("Hekki", 3);
        assertEquals(3, t.getLineNumber());
    }
    
    @Test
    public void testName() throws Exception {
        Token t = new Token("hello", 2);
        assertEquals("hello", t.getName());
        
        t = new Token("bye", 2);
        assertEquals("bye", t.getName());
    }
    
    @Test(expected=Exception.class)
    public void testInvalidLineNumberWithZero() throws Exception {
        Token t = new Token("hello", 0);
    }
    
    @Test(expected=Exception.class)
    public void testInvalidLineNumberWithNegative() throws Exception {
        Token t = new Token("aaa", -1);
    }
    
    @Test(expected=Exception.class)
    public void testWithEmptyName() throws Exception {
        Token t = new Token("", 1);
    }
}
