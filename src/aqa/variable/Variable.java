/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import aqa.value.NullValue;
import aqa.value.Value;

/**
 *
 * @author martinhart
 */
public class Variable {
    
    private final String name;
    private Value value;
    private boolean constant;
    
    public Variable(String name) {
        this.name = name;
        this.value = NullValue.instance;
        this.constant = false;
    }

    public Value getValue() {
        return this.value;
    }

    public boolean isConstant() {
        return constant;
    }

    public void makeConstant() {
        this.constant = true;
    }

    public void setValue(Value sv) throws InterpreterException {
        if (isConstant()) {
            if (value == NullValue.instance) {
                this.value = sv;
            }
            else {
                throw new InterpreterException("cannot modify constant variable " + name);
            }
        }
        else {
            this.value = sv;
        }
    }

    public String getName() {
        return name;
    }
}
