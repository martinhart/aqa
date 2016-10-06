/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import java.util.List;

/**
 * This interface defines the responsibilities of a variable table.  The VM
 * uses a variable table to maintain its current context.
 * 
 * @author martinhart
 */
public abstract class VariableTable {
    
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
     * @return the number of variables in the set.
     */
    public abstract int size();
    
    /**
     * @return a list of variable names in this table
     */
    public abstract List<String> getNames();
}
