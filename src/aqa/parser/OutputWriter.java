/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * An interface used to provide output while the program is running.
 * When the parser encounters a statement such as:
 * 
 *      OUTPUT 'Hello'
 *      or
 *      INSPECT a+b
 * 
 * The output writer is used to send the result to a UI component.
 * 
 * @author martinhart
 */
public interface OutputWriter {

    /**
     * Implement this method to send some output to the display/console/file/whatever.
     *
     * @param message information to be outputted.
     */
    public void output(String message);
}
