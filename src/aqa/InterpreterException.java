/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

/**
 *
 * @author martinhart
 */
public class InterpreterException extends Exception {
    
    private int line;

    public InterpreterException(String message) {
        super(message);
        this.line = 1;
    }
    
    public InterpreterException(int line, String message) {
        this("line " + line + ": " + message);
        this.line = line;
    }
    
    public int getLine() {
        return line;
    }
}
