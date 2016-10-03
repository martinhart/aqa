/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.InterpreterException;
import aqa.tokenizer.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class SubroutineTest {

    private Subroutine subject;

    @Before
    public void setUp() {
        subject = new Subroutine("name");
    }

    @Test
    public void testName() {
        assertEquals("name", subject.getName());
    }

    @Test
    public void initiallyHasNoParameters() {
        assertEquals(0, subject.getNumberOfParameters());
    }

    @Test
    public void addingParameterIncreasesSize() {
        subject.addParameter("Hello");
        assertEquals(1, subject.getNumberOfParameters());
        subject.addParameter("Hello");
        assertEquals(2, subject.getNumberOfParameters());
    }

    @Test
    public void addingParametersAddsToArrayList() throws InterpreterException {
        subject.addParameter("help");
        assertEquals("help", subject.getParameter(0));
    }
}
