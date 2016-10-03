/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

/**
 *
 * @author martinhart
 */
public class SubroutineException extends Exception {
 
    public SubroutineException(String message) {
        super(message);
    }
    
    public SubroutineException(String subroutineName, String message) {
        super("in subroutine '" + subroutineName + "' - " + message);
    }
}
