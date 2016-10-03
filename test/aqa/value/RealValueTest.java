/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.value.RealValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class RealValueTest {

    private RealValue subject;

    @Test
    public void testInspect() {
        subject = new RealValue(1.1);
        assertEquals("1.1", subject.inspect());
    }

    @Test
    public void testOutput() {
        subject = new RealValue(2.1);
        assertEquals("2.1", subject.output());
    }
}
