/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;

/**
 * This class is used by the Parser to notify clients when a new instruction
 * is being processed.
 * @author martinhart
 */
public class InstructionListener {
    public void newInstruction(int lineNumber, VirtualMachine vm) throws InterpreterException {
        // do nothing.
    }
}
