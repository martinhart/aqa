/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class NullInputProviderTest {
    
    NullInputProvider subject;
    
    @Before
    public void setUp() {
        subject = new NullInputProvider();
    }

    @Test
    public void testProvidesNoInput() {
        assertEquals("", subject.getInput());
    }
}
