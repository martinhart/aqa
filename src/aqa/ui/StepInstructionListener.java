/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import aqa.parser.InstructionListener;

/**
 * This class is used to step through the parser.  It intercepts the parser's
 * notification that a new instruction is being processed and blocks the worker
 * thread until released by a flag (triggered from UI menu).
 * @author martinhart
 */
public class StepInstructionListener extends InstructionListener {
    
    private InterpreterWorker worker;
    private final StepLock lock;
    
    public StepInstructionListener(StepLock lock) {
        this.lock = lock;
    }
    
    @Override
    public void newInstruction(int lineNumber) {
        boolean canContinue = false;
        
        worker.publishLine(lineNumber);
        while (false == lock.canContinue()) {
            // do nothing until we can continue!
        }
    }
    
    public void executeWorker(InterpreterWorker worker) {
        this.worker = worker;
        worker.execute();
    }
}
