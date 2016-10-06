/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;

/**
 * An interface to provide a mechanism for the parser to notify clients about
 * its progress. For example, when debugging, the parser will use this interface
 * to send information about the current line and state of the VM.
 *
 * @author martinhart
 */
public interface InstructionListener {

    /**
     * Override this method to take action when the parser moves on to a new
     * instruction.  The method will be executed immediately *before* the instruction
     * is run.
     *
     * @param lineNumber the line number in source code of the instruction to be executed next.
     * @param vm the current virtual machine
     * @throws InterpreterException throw this exception to abort execution
     */
    public void newInstruction(int lineNumber, VirtualMachine vm) throws InterpreterException;
}
