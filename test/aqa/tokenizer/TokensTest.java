/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.tokenizer.Tokens;
import aqa.tokenizer.Token;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class TokensTest {
    
    private Tokens subject;
    
    @Before
    public void setUp() {
        subject = new Tokens();
    }
    
    @Test
    public void testIsEmptyWhenEmpty() {
        assertEquals(true, subject.isEmpty());
    }
    
    @Test
    public void testIsEmptyWhenNotEmpty() throws Exception {
        subject.append(new Token("a", 1));
        assertEquals(false, subject.isEmpty());
    }
    
}
