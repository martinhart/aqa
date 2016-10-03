/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * This class ignores requests to write output.  It does nothing!
 * @author martinhart
 */
public class NullOutputWriter implements OutputWriter {

    @Override
    public void output(String message) {
    }
    
}
