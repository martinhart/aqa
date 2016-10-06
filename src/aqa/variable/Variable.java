/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import aqa.value.NullValue;
import aqa.value.Value;

/**
 * A variable is a mapping of name -> Value.
 * 
 * This class encapsulates that concept and provides an interface for working
 * with variables
 * 
 * @author martinhart
 */
public class Variable {
   
    /**
     * The name of this variable
     */
    private final String name;
    
    /**
     * The variable's current value
     */
    private Value value;
    
    /**
     * Is this variable constant?
     */
    private boolean constant;
    
    /**
     * Create a new variable with given name.  Assign it a null value.
     * @param name variable name.
     */
    public Variable(String name) {
        this.name = name;
        this.value = NullValue.instance;
        this.constant = false;
    }

    /**
     * @return the variable's value
     */
    public Value getValue() {
        return this.value;
    }

    /**
     * @return true if this is a constant variable
     */
    public boolean isConstant() {
        return constant;
    }

    /**
     * Make this variable constant.
     */
    public void makeConstant() {
        this.constant = true;
    }

    /**
     * Change the value of this variable
     * @param sv new value
     * @throws InterpreterException if it's constant and cannot be changed
     */
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

    /**
     * @return the name of this variable
     */
    public String getName() {
        return name;
    }
}
