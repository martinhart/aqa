/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * This class provides NO input to the interpreter.  It simply returns an
 * empty string.  It is unwise to use in production unless you are *certain*
 * that the source program does not contain USERINPUT statements.
 * 
 * It is useful in testing to avoid the program sitting there dumbly waiting for
 * you to type something!
 * 
 * @author martinhart
 */
public class NullInputProvider implements InputProvider {

    @Override
    public String getInput() {
        return "";
    }
    
}
