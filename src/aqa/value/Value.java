/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 * A Value is a placeholder for a specific type and value of data.  
 * This interface defines the methods that all values should implement.
 * 
 * @author martinhart
 */
public interface Value {    
    
    /**
     * Provide a debugger friendly string representation of the value
     * @return output string
     * @throws InterpreterException if an error occurs
     */
    public String inspect() throws InterpreterException;
    
    /**
     * Provide a user friendly string representation of the underlying value.
     * For example
     * 
     *  OUTPUT 1+2
     * 
     * should provide the string "3"
     * 
     * @return output string
     * @throws InterpreterException if an error occurs
     */
    public String output() throws InterpreterException;
    
    /**
     * Compare this with another value
     * @param other the value to compare with
     * @return -1 if this < other, 0 if this == other, 1 if this > other
     * @throws InterpreterException if comparison cannot occur (e.g. comparing integer with string)
     */
    public int compare(Value other) throws InterpreterException;
    
    /**
     * Test for equality
     * @param other the value to test with
     * @return true if underlying values are equal, false if not.
     * @throws InterpreterException if test is meaningless (e.g. comparing array with string)
     */
    public boolean equal(Value other) throws InterpreterException;
    
    /**
     * Make a copy of this value.  To enable pass by value arguments.
     * 
     * TODO: refactor this away and live with pass by reference.
     * 
     * @return the copied value
     * @throws InterpreterException if an error occurs creating a copy
     */
    public Value makeCopy() throws InterpreterException;
    
    /**
     * get the underlying value of this Value object
     * @return the value or null.
     */
    public Object getValue();
    
    /**
     * Add this value to other
     * @param other the value to add
     * @return the result
     * @throws InterpreterException if add is unachievable (e.g. '1' + 3.4)
     */
    public Value add(Value other) throws InterpreterException;
    
    /**
     * Subtract other from this value
     * @param other the value to subtract
     * @return the result
     * @throws InterpreterException if subtract is not possible (e.g. [] - 'hi')
     */
    public Value subtract(Value other) throws InterpreterException;
    
    /**
     * multiply this by other
     * @param other value to multiply with
     * @return the result
     * @throws InterpreterException if operation cannot occur
     */
    public Value multiply(Value other) throws InterpreterException;
    
    /**
     * divide this by other.  Use real division.
     * @param other value to divide by
     * @return the result
     * @throws InterpreterException if operation cannot be carried out
     */
    public Value divide(Value other) throws InterpreterException;
    
    /**
     * divide this by other.  Use integer division.
     * @param other value to div by
     * @return the result
     * @throws InterpreterException if operation cannot be carried out
     */
    public Value div(Value other) throws InterpreterException;
    
    /**
     * find remainder of division
     * @param other value to MOD with
     * @return the result
     * @throws InterpreterException if operation cannot be carried out
     */
    public Value mod(Value other) throws InterpreterException;
    
    /**
     * return the length of this value
     * @return the result
     * @throws InterpreterException if operation cannot be carried out
     */
    public IntegerValue length() throws InterpreterException;
        
    /**
     * convert this value to a string
     * @return the result
     * @throws InterpreterException if operation cannot be carried out
     */
    public StringValue toStr() throws InterpreterException;
    
    /**
     * store a value at given index inside this.
     * @param index the index to store at
     * @param item the item to store
     * @throws InterpreterException if not achievable (e.g. this is not an array)
     */
    public void setAtIndex(int index, Value item) throws InterpreterException;
    
    /**
     * retrieve value at given index inside this.
     * 
     * @param index the index to retrieve from
     * @return the value at given index
     * @throws InterpreterException if not achievable (e.g. this is not an array)
     */
    public Value getAtIndex(int index) throws InterpreterException;
}
