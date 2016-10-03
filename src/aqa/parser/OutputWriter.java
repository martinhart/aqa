/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * This interface is triggered by the Interpreter when stuff should be
 * outputted.
 *
 * @author martinhart
 */
public interface OutputWriter {

    /**
     * Implement this method to send some output to the display.
     *
     * @param message information to be outputted.
     */
    public void output(String message);
}
