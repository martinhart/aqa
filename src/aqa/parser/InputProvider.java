/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 *
 * @author martinhart
 */
public interface InputProvider {

    /**
     * Provide some user input to the interpreter by overriding this method.
     *
     * @return user's input.
     */
    String getInput();
}
