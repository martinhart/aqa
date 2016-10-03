/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.value.StringValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class StringValueTest {
 
    private StringValue subject;

    @Test
    public void testInspect() {
        subject = new StringValue("Woo");
        assertEquals("'Woo'", subject.inspect());
    }

    @Test
    public void testOutput() {
        subject = new StringValue("Woo");
        assertEquals("Woo", subject.output());
    }
}
