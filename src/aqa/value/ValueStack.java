/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 *
 * @author martinhart
 */
public abstract class ValueStack {
    
    public abstract void push(Value v);
    public abstract Value pop() throws InterpreterException;
    public abstract Value peek() throws InterpreterException;

    public void push(boolean b) {
        push(new BooleanValue(b));
    }

    public void push(int i) {
        push(new IntegerValue(i));
    }

    public void push(double d) {
        push(new RealValue(d));
    }

    public void push(String s) {
        push(new StringValue(s));
    }

    public boolean popBoolean() throws InterpreterException {
        Value v = pop();
        try {
            BooleanValue bv = (BooleanValue) v;
            return bv.value;
        }
        catch(ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be boolean");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a boolean");
            }
        }
    }

    public int popInteger() throws InterpreterException {
        Value v = pop();
        try {
            IntegerValue iv = (IntegerValue) v;
            return iv.value;
        }
        catch(ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be integer");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a integer");
            }
        }
    }

    public String popString() throws InterpreterException {
        Value v = pop();
        try {
            StringValue sv = (StringValue) v;
            return sv.value;
        }
        catch(ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be string");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a string");
            }
        }
    }

    public double popReal() throws InterpreterException {
        Value v = pop();
        try {
            RealValue rv = (RealValue) v;
            return rv.value;
        }
        catch(ClassCastException e) {
            try {
                throw new InterpreterException("expecting " + v.inspect() + " to be real");
            } catch (InterpreterException ex) {
                throw new InterpreterException("unexpected request for a real");
            }
        }
    }   
}
