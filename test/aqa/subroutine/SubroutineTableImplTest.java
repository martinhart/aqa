/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.InterpreterException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class SubroutineTableImplTest {
    
    private SubroutineTableImpl subject;
    
    @Before
    public void setUp() {
        subject = new SubroutineTableImpl();
    }
    
    @Test
    public void testAddingNonExistant() throws InterpreterException {
        Subroutine s = new Subroutine("a");
        subject.add(s);
        assertEquals(s, subject.get("a"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testAddingDuplicate() throws InterpreterException {
        subject.add(new Subroutine("a"));
        subject.add(new Subroutine("a"));
    }
 
    @Test
    public void testAddingNonDuplicate() throws InterpreterException {
        Subroutine s;
        subject.add(new Subroutine("a"));
        subject.add(new Subroutine("b"));
        s = subject.get("b");
    }
    
    @Test(expected=InterpreterException.class)
    public void testGettingNonExisting() throws InterpreterException {
        subject.get("not here");
    }

}
