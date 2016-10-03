/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.InterpreterException;

/**
 *
 * @author martinhart
 */
public interface Subroutines {

    /**
     * Add a subroutine. AQA prohibits adding more than one subroutine with the
     * same name -
     *
     * @param s the Subroutine to add.
     * @throws aqa.InterpreterException if subroutine with same name
     * already added.
     */
    public void add(Subroutine s) throws InterpreterException;

    /**
     * Retrieve a Subroutine by name.
     *
     * @param name the name of the Subroutine.
     * @return the Subroutine.
     * @throws aqa.InterpreterException if there is no such
     * subroutine.
     */
    public Subroutine get(String name) throws InterpreterException;

}
