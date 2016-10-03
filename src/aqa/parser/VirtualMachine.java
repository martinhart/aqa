/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.subroutine.Subroutine;
import aqa.subroutine.Subroutines;
import aqa.subroutine.SubroutinesImpl;
import aqa.value.Value;
import aqa.value.ValueStack;
import aqa.value.ValueStackImpl;
import aqa.variable.Variable;
import aqa.variable.Variables;
import aqa.variable.VariablesImpl;

/**
 * This class wraps up the state of a single virtual machine. It's used to keep
 * track of values, variables and subroutines definitions.
 *
 * @author martinhart
 */
public class VirtualMachine {

    private final ValueStack values;
    private final Variables variables;
    private final Subroutines subroutines;

    /**
     * Create a brand new virtual machine with no context.
     */
    public VirtualMachine() {
        this(new SubroutinesImpl());
    }

    /**
     * Create a new virtual machine with subroutine context (e.g. to execute a
     * subroutine)
     *
     * @param s subroutines to use.
     */
    public VirtualMachine(Subroutines s) {
        this(s, new VariablesImpl());
    }

    /**
     * Create a new virtual machine with subroutine and variables context
     *
     * @param s subroutines to use
     * @param v variables to use
     */
    public VirtualMachine(Subroutines s, Variables v) {
        this(s, v, new ValueStackImpl());
    }
    
    public VirtualMachine(Subroutines s, Variables v, ValueStack va) {
        values = va;
        variables = v;
        subroutines = s;
    }

    /**
     * Retrieve the value stack being used by this VM
     *
     * @return
     */
    public ValueStack getValueStack() {
        return values;
    }

    public void pushValue(Value v) {
        values.push(v);
    }

    public Value popValue() throws InterpreterException {
        return values.pop();
    }

    public Value peekValue() throws InterpreterException {
        return values.peek();
    }

    /**
     * Retrieve the variable table being used by this VM
     *
     * @return
     */
    public Variables getVariables() {
        return variables;
    }

    /**
     * Retrieve the subroutine table being used by this VM
     *
     * @return
     */
    public Subroutines getSubroutines() {
        return subroutines;
    }

    /**
     * Add a variable to this VM. Defers to Variables
     *
     * @param v
     * @throws aqa.InterpreterException
     */
    public void addVariable(Variable v) throws InterpreterException {
        variables.set(v);
    }

    /**
     * Retrieve a variable from this VM. Defers to Variables
     *
     * @param name name of variable
     * @return
     */
    public Variable getVariable(String name) {
        return variables.get(name);
    }

    /**
     * Add a subroutine to this VM. Defers to Subroutines
     *
     * @param s
     * @throws aqa.InterpreterException
     */
    public void addSubroutine(Subroutine s) throws InterpreterException {
        subroutines.add(s);
    }

    /**
     * Retrieve a subroutine from this VM. Defers to Subroutines
     *
     * @param name name of subroutine
     * @return
     * @throws InterpreterException
     */
    public Subroutine getSubroutine(String name) throws InterpreterException {
        return subroutines.get(name);
    }

}
