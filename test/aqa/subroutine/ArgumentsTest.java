/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.value.ValueBase;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class ArgumentsTest {

    private Arguments subject;

    @Before
    public void setUp() {
        subject = new Arguments();
    }

    @Test
    public void initialLength() {
        assertEquals(0, subject.length());
    }

    @Test
    public void adding1Item() {
        Argument a = new Argument(new StubValue());
        subject.add(a);
        assertEquals(1, subject.length());
    }

    @Test
    public void getting1Item() throws ArgumentException {
        Argument a = new Argument(new StubValue());
        subject.add(a);
        assertEquals(a, subject.get(0));
    }
    
    @Test
    public void gettingMultipleItems() throws ArgumentException {
        Argument a1 = new Argument(new StubValue());
        Argument a2 = new Argument(new StubValue());
        subject.add(a1);
        subject.add(a2);
        assertEquals(a1, subject.get(0));
        assertEquals(a2, subject.get(1));
    }
    
    @Test(expected=ArgumentException.class)
    public void gettingInvalidIndex() throws ArgumentException {
        subject.get(0);
    }

    private class StubValue extends ValueBase {

        public StubValue() {
            super("STUB");
        }

    }

}
