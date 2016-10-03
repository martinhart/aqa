/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * This interface is triggered by the Interpreter when stuff should be outputted.
 * @author martinhart
 */
public interface OutputWriter {
    public void output(String message);
}
