/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * This class represents a stack of values to be used by a virtual machine.
 *
 * @author martinhart
 */
public class ValueStackImpl extends ValueStack {

    private final Stack<Value> stack;

    public ValueStackImpl() {
        this.stack = new Stack<>();
    }

    @Override
    public void push(Value v) {
        stack.push(v);
    }

    @Override
    public Value pop() throws InterpreterException {
        try {
            return stack.pop();
        }
        catch(EmptyStackException e) {
            throw new InterpreterException(e.getLocalizedMessage());
        }
    }

    @Override
    public Value peek() throws InterpreterException {
        try {
            return stack.peek();
        } catch(EmptyStackException e) {
            throw new InterpreterException(e.getLocalizedMessage());
        }
    }
}
