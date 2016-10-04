/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.tokenizer.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class ParserTest {
    
    private Parser subject;
    private InstructionListenerStub instructionListener;
    
    @Test
    public void testInstructionListenerIsCalledWithCorrectLines() throws InterpreterException {
        instructionListener = new InstructionListenerStub();
        subject = new Parser(instructionListener);
        subject.addToken(new Token("1", 1));
        subject.addToken(new Token("2", 100));
        subject.parse();
        
        assertEquals(2, instructionListener.lines.size());
        assertEquals(new Integer(1), instructionListener.lines.get(0));
        assertEquals(new Integer(100), instructionListener.lines.get(1));
    }

    class InstructionListenerStub extends InstructionListener {
        
        public final List<Integer> lines = new ArrayList<>();
        
        @Override
        public void newInstruction(int line) {
            lines.add(line);
        }
    }
}
