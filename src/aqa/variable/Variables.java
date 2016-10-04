/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import java.util.List;

/**
 *
 * @author martinhart
 */
public abstract class Variables {
    
    /**
     * Store variable in var table.
     *
     * @param var the variable to store
     * @throws InterpreterException if there is already an item with same name
     */
    public abstract void set(Variable var) throws InterpreterException;

    /**
     * Return the variable so named. Create a variable and store in the set if
     * there is no such variable.
     *
     * @param name
     * @return the Variable named name.
     */
    public abstract Variable get(String name);   
    
    /**
     * Return the number of variables in the set.
     */
    public abstract int size();
    
    /**
     * Return the variable names
     */
    public abstract List<String> getNames();
}
