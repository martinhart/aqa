/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.InterpreterException;

/**
 * This interface provides a way of maintaining a table of defined subroutines.
 * 
 * @author martinhart
 */
public interface SubroutineTable {

    /**
     * Add a subroutine. AQA prohibits adding more than one subroutine with the
     * same name - or at least that's how I've interpreted the spec.
     *
     * @param s the Subroutine to add.
     * @throws aqa.InterpreterException if subroutine with same name
     * already exists.
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
