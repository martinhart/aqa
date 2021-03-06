/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 * This class implements the full Value interface and can be used as a base
 * for all Value types.  It provides default behaviour for all of the Value
 * methods - which is basically to throw an exception.
 * 
 * You can derive from this class when creating a new Value type and override the
 * specific methods that make sense for your type.
 * 
 * @author martinhart
 */
public class ValueBase implements Value {
    
    private final String type;
    
    /**
     * Construct with the type of your child.
     * @param type the type of the value (e.g. "int", "real" etc)
     */
    public ValueBase(String type) {
        this.type = type;
    }
   
    @Override
    public String inspect() throws InterpreterException {
        return "#<" + type + ">";
    }

    @Override
    public String output() throws InterpreterException {
        bugout("output");
        return null;
    }

    @Override
    public int compare(Value other) throws InterpreterException {
        bugout("cannot compare with " + other.inspect());
        return 0;
    }

    @Override
    public boolean equal(Value other) throws InterpreterException {
        bugout("equal");
        return false;
    }

    @Override
    public Value add(Value other) throws InterpreterException {
        bugout("add");
        return null;
    }

    @Override
    public Value subtract(Value other) throws InterpreterException {
        bugout("subtract");
        return null;
    }

    @Override
    public Value multiply(Value other) throws InterpreterException {
        bugout("multiply");
        return null;
    }

    @Override
    public Value divide(Value other) throws InterpreterException {
        bugout("divide");
        return null;
    }

    @Override
    public Value div(Value other) throws InterpreterException {
        bugout("div");
        return null;
    }

    @Override
    public Value mod(Value other) throws InterpreterException {
        bugout("mod");
        return null;
    }

    @Override
    public IntegerValue length() throws InterpreterException {
        bugout("length");
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public StringValue toStr() throws InterpreterException {
        bugout("tostr");
        return null;
    }

    @Override
    public void setAtIndex(int index, Value item) throws InterpreterException {
        bugout("setatindex");
    }

    @Override
    public Value getAtIndex(int index) throws InterpreterException {
        bugout("getatindex");
        return null;
    }

    @Override
    public Value makeCopy() throws InterpreterException {
        return this;
    }

    /**
     * Throw an exception with a helpful error message that can aid the user
     * in working out how they've violated the language rules.
     * @param fn The name of the function being called.
     * @throws InterpreterException containing error message.
     */
    protected void bugout(String fn) throws InterpreterException {
        throw new InterpreterException(fn + ": invalid operation on " + type);
    }    

}
