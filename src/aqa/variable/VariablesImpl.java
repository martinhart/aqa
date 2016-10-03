/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author martinhart
 */
public class VariablesImpl extends Variables {

    private final HashMap<String, Variable> variables;

    public VariablesImpl() {
        variables = new HashMap<>();
    }

    @Override
    public void set(Variable var) throws InterpreterException {
        if (variables.containsKey(var.getName())) {
            throw new InterpreterException("variable '" + var.getName() + "' already exists");
        }
        variables.put(var.getName(), var);
    }

    @Override
    public Variable get(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else {
            Variable v = new Variable(name);
            variables.put(name, v);
            return v;
        }
    }

    @Override
    public Iterator<Variable> iterator() {
        return variables.values().iterator();
    }
}
