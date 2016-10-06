/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 * The virtual machine needs to maintain a stack of values in order to process
 * instructions. For example the following statement:
 *
 * 1 + 2 + 3
 *
 * requires the VM to execute:
 *
 * PUSH 1 PUSH 2 ADD(POP, POP) PUSH RESULT ADD(POP, POP) PUSH RESULT
 *
 *
 * This class represents an interface to the value stack.
 *
 * @author martinhart
 */
public abstract class ValueStack {

    /**
     * Push a new value onto the stack
     *
     * @param v the value
     */
    public abstract void push(Value v);

    /**
     * Pop the top value from the stack
     *
     * @return the value
     * @throws InterpreterException if the stack is empty
     */
    public abstract Value pop() throws InterpreterException;

    /**
     * Peek at the top value on stack
     *
     * @return the value
     * @throws InterpreterException if the stack is empty
     */
    public abstract Value peek() throws InterpreterException;

    /**
     * Push a boolean value onto the stack
     *
     * @param b the underlying value
     */
    public void push(boolean b) {
        push(ValueFactory.createBoolean(b));
    }

    /**
     * Push an integer value onto the stack
     *
     * @param i the underlying value
     */
    public void push(int i) {
        push(ValueFactory.createInteger(i));
    }

    /**
     * Push a double value onto the stack
     *
     * @param d the underlying value
     */
    public void push(double d) {
        push(ValueFactory.createReal(d));
    }

    /**
     * Push a string value onto the stack
     *
     * @param s the underlying value
     */
    public void push(String s) {
        push(ValueFactory.createString(s));
    }

    /**
     * Pop a boolean value from the stack
     *
     * @return the underlying value
     * @throws InterpreterException if stack is empty or value is not boolean
     */
    public boolean popBoolean() throws InterpreterException {
        Value v = pop();
        try {
            BooleanValue bv = (BooleanValue) v;
            return bv.value;
        } catch (ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be boolean");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a boolean");
            }
        }
    }

    /**
     * Pop an integer value from the stack
     *
     * @return the underlying value
     * @throws InterpreterException if stack is empty or value is not integer
     */
    public int popInteger() throws InterpreterException {
        Value v = pop();
        try {
            IntegerValue iv = (IntegerValue) v;
            return iv.value;
        } catch (ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be integer");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a integer");
            }
        }
    }

    /**
     * Pop a string value from the stack
     *
     * @return the underlying value
     * @throws InterpreterException if stack is empty or value is not string
     */
    public String popString() throws InterpreterException {
        Value v = pop();
        try {
            StringValue sv = (StringValue) v;
            return sv.value;
        } catch (ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be string");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a string");
            }
        }
    }

    /**
     * Pop a double value from the stack
     *
     * @return the underlying value
     * @throws InterpreterException if stack is empty or value is not double
     */
    public double popReal() throws InterpreterException {
        Value v = pop();
        try {
            RealValue rv = (RealValue) v;
            return rv.value;
        } catch (ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be real");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a real");
            }
        }
    }
}
