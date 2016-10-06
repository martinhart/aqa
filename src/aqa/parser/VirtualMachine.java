/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.subroutine.Subroutine;
import aqa.subroutine.SubroutineTableImpl;
import aqa.value.Value;
import aqa.value.ValueStack;
import aqa.value.ValueStackImpl;
import aqa.variable.Variable;
import aqa.variable.VariableTable;
import aqa.variable.VariableTableImpl;
import aqa.subroutine.SubroutineTable;

/**
 * When a program is being executed it will contain:
 *
 * * A variable table (mappings of variable names to values) * A value stack
 * (progress of current instruction) * A subroutine table (mappings of
 * subroutine names to code).
 *
 * This class wraps up the state of a single virtual machine. It's basically a
 * delegator used to minimise the amount of stuff passed between the parser and
 * its clients.
 * 
 * Each instance has its own value stack but can be passed variables and subroutines
 * from another context.
 *
 * @author martinhart
 */
public class VirtualMachine {

    /**
     * The value stack being used by the current context.
     */
    private final ValueStack values;

    /**
     * The variables that are visible in the current context.
     */
    private final VariableTable variables;

    /**
     * The subroutines that are visible in the current context.
     */
    private final SubroutineTable subroutines;

    /**
     * Create a new virtual machine with full context.
     * @param s the subroutines to use in this vm
     * @param v the variables to use in this vm
     * @param va the value stack to use inside this vm.
     */
    public VirtualMachine(SubroutineTable s, VariableTable v, ValueStack va) {
        values = va;
        variables = v;
        subroutines = s;
    }

    /**
     * Create a new virtual machine with subroutine and variables context (e.g.
     * to execute a subroutine with arguments)
     *
     * @param s subroutines to use
     * @param v variables to use
     */
    public VirtualMachine(SubroutineTable s, VariableTable v) {
        this(s, v, new ValueStackImpl());
    }

    /**
     * Create a new virtual machine with subroutine context (e.g. to execute a
     * subroutine)
     *
     * @param s subroutines to use.
     */
    public VirtualMachine(SubroutineTable s) {
        this(s, new VariableTableImpl());
    }

    /**
     * Create a brand new virtual machine with no context. (e.g. to execute a
     * top level program).
     */
    public VirtualMachine() {
        this(new SubroutineTableImpl());
    }


    /**
     * Retrieve the value stack being used by this VM
     *
     * @return
     */
    public ValueStack getValueStack() {
        return values;
    }

    /**
     * Add a value to the value stack
     * @param v the value to add
     */
    public void pushValue(Value v) {
        values.push(v);
    }

    /**
     * Pop the top value from the value stack
     * @return the value
     * @throws InterpreterException if stack is empty
     */
    public Value popValue() throws InterpreterException {
        return values.pop();
    }

    /**
     * Peek at the top value on the value stack
     * @return the value
     * @throws InterpreterException if stack is empty
     */
    public Value peekValue() throws InterpreterException {
        return values.peek();
    }

    /**
     * Retrieve the variable table being used by this VM
     *
     * @return var table
     */
    public VariableTable getVariables() {
        return variables;
    }

    /**
     * Retrieve the subroutine table being used by this VM
     *
     * @return subroutine table
     */
    public SubroutineTable getSubroutineTable() {
        return subroutines;
    }

    /**
     * Add a variable to this VM.
     *
     * @param v the variable to add
     * @throws aqa.InterpreterException if variable already exists
     */
    public void addVariable(Variable v) throws InterpreterException {
        variables.set(v);
    }

    /**
     * Retrieve a variable from this VM.
     *
     * @param name name of variable
     * @return the variable
     */
    public Variable getVariable(String name) {
        return variables.get(name);
    }

    /**
     * Add a subroutine to this VM.
     *
     * @param s the subroutine to add
     * @throws aqa.InterpreterException if subroutine already exists
     */
    public void addSubroutine(Subroutine s) throws InterpreterException {
        subroutines.add(s);
    }

    /**
     * Retrieve a subroutine from this VM.
     *
     * @param name name of subroutine
     * @return the subroutine
     * @throws InterpreterException if there isn't one with given name
     */
    public Subroutine getSubroutine(String name) throws InterpreterException {
        return subroutines.get(name);
    }
}
