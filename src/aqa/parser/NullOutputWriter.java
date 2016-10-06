/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * This class IGNORES requests to write output.  It does nothing!
 * 
 * It is unwise to use this class in production unless you are certain that the
 * program contains no OUTPUT or INSPECT statements - as they won't have any
 * effect.
 * 
 * It is useful in testing.
 * 
 * @author martinhart
 */
public class NullOutputWriter implements OutputWriter {

    @Override
    public void output(String message) {
        // do nothing
    }
    
}
