/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 *
 * @author martinhart
 */
public interface Value {    
    public String inspect() throws InterpreterException;
    public String output() throws InterpreterException;
    public int compare(Value other) throws InterpreterException;
    public boolean equal(Value other) throws InterpreterException;
    
    public Value makeCopy() throws InterpreterException;
    
    /**
     * get the underlying value of this Value object
     * @return the value or null.
     */
    public Object getValue();
    
    public Value add(Value other) throws InterpreterException;
    public Value subtract(Value other) throws InterpreterException;
    public Value multiply(Value other) throws InterpreterException;
    public Value divide(Value other) throws InterpreterException;
    public Value div(Value other) throws InterpreterException;
    public Value mod(Value other) throws InterpreterException;
    
    public IntegerValue length() throws InterpreterException;
        
    public StringValue toStr() throws InterpreterException;
    
    public void setAtIndex(int index, Value item) throws InterpreterException;
    public Value getAtIndex(int index) throws InterpreterException;
}
