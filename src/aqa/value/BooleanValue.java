/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 * This class represents a boolean
 * @author martinhart
 */
public class BooleanValue extends ValueBase {
    
    public final boolean value;
    
    public BooleanValue(boolean value) {
        super("boolean");
        this.value = value;
    }

    @Override
    public String inspect() {
        if (value) {
            return "TRUE";
        }
        return "FALSE";
    }

    @Override
    public String output() {
        return inspect();
    }

    @Override
    public boolean equal(Value other) throws InterpreterException {
        if (other instanceof BooleanValue) {
            BooleanValue o = (BooleanValue) other;
            return (this.value == o.value);
        }
        return super.equal(other);
    }

    @Override
    public Object getValue() {
        return value;
    }
}
