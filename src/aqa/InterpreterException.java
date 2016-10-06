/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

/**
 * A wrapper class for the types of exception that may occur when running
 * a program.  Problems might come from tokenising, parsing and/or
 * interpreting the actual statements.
 * @author martinhart
 */
public class InterpreterException extends Exception {
    
    private final int line;

    /**
     * Create an exception with an error message.  Since there is no line
     * number context the line is set to the start of the source code.
     * @param message the user-facing error message
     */
    public InterpreterException(String message) {
        super(message);
        this.line = 1;
    }
    
    /**
     * Create an exception with a source line number and error message
     * @param line the line number on which a problem occurred
     * @param message the user-facing error message
     */
    public InterpreterException(int line, String message) {
        super("line " + line + ": " + message);
        this.line = line;
    }
    
    /**
     * @return the line number in the source code that caused this exception
     */
    public int getLine() {
        return line;
    }
}
