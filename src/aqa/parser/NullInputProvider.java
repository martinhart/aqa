/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * This class provides NO input to the interpreter.  It simply returns an
 * empty string.
 * @author martinhart
 */
public class NullInputProvider implements InputProvider {

    @Override
    public String getInput() {
        return "";
    }
    
}
