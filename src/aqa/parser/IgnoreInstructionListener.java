/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;

/**
 * This class does nothing when the parser notifies clients about a new
 * instruction.  It can be used for running the program without breakpoints/debugging.
 * 
 * @author martinhart
 */
public class IgnoreInstructionListener implements InstructionListener {
    
    @Override
    public void newInstruction(int lineNumber, VirtualMachine vm) throws InterpreterException {
        // do nothing.
    }
}
