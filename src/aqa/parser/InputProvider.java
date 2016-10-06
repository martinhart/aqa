/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * An interface used to provide user input while the program is running.
 * When the parser encounters a statement such as:
 * 
 *      a <- USERINPUT
 * 
 * The input provider is used to obtain some actual input from the user to store
 * in the variable a.
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
