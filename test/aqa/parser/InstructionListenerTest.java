/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class InstructionListenerTest {
    
    @Test
    public void testNewInstruction() throws InterpreterException {
        new InstructionListener().newInstruction(10, null);
    }
    
    @Test
    public void testOverrideOfNewInstruction() throws InterpreterException {
        InstructionListenerStub s = new InstructionListenerStub();
        InstructionListener base = s;
        base.newInstruction(100, null);
        assertEquals(100, s.lineNumber);
    }
    
    private class InstructionListenerStub extends InstructionListener {
        public int lineNumber;
        @Override
        public void newInstruction(int line, VirtualMachine vm) {
            lineNumber = line;
        }
    }
}
