/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides a concrete implementation of the VariableTable interface
 * for use with the virtual machine.
 * 
 * @author martinhart
 */
public class VariableTableImpl extends VariableTable {

    /**
     * The variables being stored.
     */
    private final HashMap<String, Variable> variables;

    public VariableTableImpl() {
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
    public int size() {
        return variables.size();
    }

    @Override
    public List<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for (String name : variables.keySet()) {
            names.add(name);
        }
        return names;
    }
}
