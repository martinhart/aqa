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
public class IgnoreInstructionListenerTest {
    
    @Test
    public void testNewInstruction() throws InterpreterException {
        new IgnoreInstructionListener().newInstruction(10, null);
    }
    
    @Test
    public void testOverrideOfNewInstruction() throws InterpreterException {
        InstructionListenerStub s = new InstructionListenerStub();
        IgnoreInstructionListener base = s;
        base.newInstruction(100, null);
        assertEquals(100, s.lineNumber);
    }
    
    private class InstructionListenerStub extends IgnoreInstructionListener {
        public int lineNumber;
        @Override
        public void newInstruction(int line, VirtualMachine vm) {
            lineNumber = line;
        }
    }
}
