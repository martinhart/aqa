/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import aqa.value.NullValue;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class VariablesImplTest {

    private VariablesImpl subject;

    @Before
    public void setUp() {
        subject = new VariablesImpl();
    }

    @Test
    public void getCreatesIfNotPresent() {
        Variable v = subject.get("a");
        assertNotNull(v);
        assertEquals(true, v.getValue() instanceof NullValue);
    }
    
    @Test
    public void sameInstanceReturnedWithSameName() {
        Variable v1 = subject.get("a");
        Variable v2 = subject.get("a");
        assertEquals(v1, v2);
    }
    
    @Test
    public void differentInstanceReturnedWithDifferentNames() {
        Variable v1 = subject.get("a");
        Variable v2 = subject.get("b");
        assertNotEquals(v1, v2);
    }
    
    @Test(expected=InterpreterException.class)
    public void cannotCreateTwoDifferentVariablesWithTheSameName() throws InterpreterException {
        Variable v1 = new Variable("a");
        Variable v2 = new Variable("a");
        subject.set(v1);
        subject.set(v2);
    }
    
    @Test
    public void testSize() throws InterpreterException {
        assertEquals(0, subject.size());
        subject.set(new Variable("a"));
        assertEquals(1, subject.size());
    }
    
    @Test
    public void testGetNames() throws InterpreterException {
        List<String> names;
        
        subject.set(new Variable("a"));
        names = subject.getNames();
        assertEquals(1, names.size());
        assertEquals("a", names.get(0));
    }
}
