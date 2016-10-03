/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 *
 * @author martinhart
 */
public class StringValue extends ValueBase {
    
    final public String value;
    
    public StringValue(String value) {
        super("string");
        this.value = value;
    }

    @Override
    public String inspect() {
        return "'" + value + "'";
    }

    @Override
    public String output() {
        return value;
    }
    
    @Override
    public String toString() {
        return inspect();
    }

    @Override
    public boolean equal(Value other) throws InterpreterException {
        if (other instanceof StringValue) {
            StringValue o = (StringValue) other;
            return value.equals(o.value);
        }
        else {
            return super.equal(other);
        }
    }

    @Override
    public Value add(Value other) throws InterpreterException {
        if (other instanceof StringValue) {
            StringValue o = (StringValue) other;
            return new StringValue(this.value + o.value);
        }
        else {
            return super.add(other);
        }
    }

    @Override
    public IntegerValue length() throws InterpreterException {
        return new IntegerValue(value.length());
    }

    @Override
    public Object getValue() {
        return value;
    }
}
