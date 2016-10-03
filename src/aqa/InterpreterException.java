/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

/**
 * This exception type is thrown if there are any problems executing the
 * given pseudocode.  Problems might come from tokenising, parsing and/or
 * interpreting the actual statements.
 * @author martinhart
 */
public class InterpreterException extends Exception {
    
    private final int line;

    public InterpreterException(String message) {
        super(message);
        this.line = 1;
    }
    
    public InterpreterException(int line, String message) {
        super("line " + line + ": " + message);
        this.line = line;
    }
    
    public int getLine() {
        return line;
    }
}
