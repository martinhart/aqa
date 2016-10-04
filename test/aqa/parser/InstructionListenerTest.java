/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class InstructionListenerTest {
    
    @Test
    public void testNewInstruction() {
        new InstructionListener().newInstruction(10);
    }
    
    @Test
    public void testOverrideOfNewInstruction() {
        InstructionListenerStub s = new InstructionListenerStub();
        InstructionListener base = s;
        base.newInstruction(100);
        assertEquals(100, s.lineNumber);
    }
    
    private class InstructionListenerStub extends InstructionListener {
        public int lineNumber;
        @Override
        public void newInstruction(int line) {
            lineNumber = line;
        }
    }
}
