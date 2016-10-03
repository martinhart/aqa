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
public class NullOutputWriterTest {
    
    @Test
    public void testOutput() {
        new NullOutputWriter().output("hello");
    }
    
}
