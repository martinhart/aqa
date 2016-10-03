/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import aqa.parser.VirtualMachine;
import aqa.subroutine.Subroutine;
import aqa.subroutine.Subroutines;
import aqa.subroutine.SubroutinesImpl;
import aqa.value.Value;
import aqa.value.ValueBase;
import aqa.value.ValueStack;
import aqa.value.ValueStackImpl;
import aqa.variable.Variable;
import aqa.variable.Variables;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author martinhart
 */
public class VirtualMachineTest {

    private VirtualMachine subject;
    private SubroutinesStub subroutines;
    private VariablesStub variables;
    private ValueStackStub values;

    @Before
    public void setUp() {
        subroutines = new SubroutinesStub();
        variables = new VariablesStub();
        values = new ValueStackStub();
        subject = new VirtualMachine(subroutines, variables, values);
    }

    @Test
    public void pushValueDefersToValueStack() {
        Value v = new StubValue();
        subject.pushValue(v);
        assertEquals(v, values.pushCalledWith);
    }
    
    @Test
    public void popValueDefersToValueStack() throws InterpreterException {
        subject.popValue();
        assertEquals(true, values.popCalled);
    }
    
    @Test
    public void peekValueDefersToValueStack() throws InterpreterException {
        subject.peekValue();
        assertEquals(true, values.peekCalled);
    }
    
    @Test
    public void addVariableDefersToVariablesInstance() throws InterpreterException {
        Variable v = new Variable("a");
        subject.addVariable(v);
        assertEquals(v, variables.setCalledWith);
    }

    @Test
    public void getVariableDefersToVariablesInstance() {
        subject.getVariable("a");
        assertEquals("a", variables.getCalledWith);
    }

    @Test
    public void addSubroutineDefersToSubroutinesInstance() throws InterpreterException {
        Subroutine s = new Subroutine("s");
        subject.addSubroutine(s);
        assertEquals(s, subroutines.addCalledWith);
    }

    @Test
    public void getSubroutineDefersToSubroutinesInstance() throws InterpreterException {
        subject.getSubroutine("aa");
        assertEquals("aa", subroutines.getCalledWith);

    }
    
    @Test
    public void getVariablesReturnsVariables() {
        assertEquals(true, subject.getVariables() instanceof Variables);
    }

    @Test
    public void getSubroutinesReturnsSubroutines() {
        assertEquals(true, subject.getSubroutines() instanceof Subroutines);
    }
    
    @Test
    public void constructingWithSubroutines() throws InterpreterException {
        createSubjectWithSubroutines();
        assertNotNull(subject.getSubroutine("1"));
        assertNotNull(subject.getSubroutine("2"));
    }    

    @Test
    public void getValueStack() {
        assertEquals(true, subject.getValueStack() instanceof ValueStack);
        assertNotEquals(subject.getValueStack(), new VirtualMachine().getValueStack());
    }

    private void createSubjectWithSubroutines() throws InterpreterException {
        Subroutines s = new SubroutinesImpl();
        s.add(new Subroutine("1"));
        s.add(new Subroutine("2"));
        subject = new VirtualMachine(s);
    }
    
    private class VariablesStub extends Variables {

        public Variable setCalledWith;
        public String getCalledWith;

        @Override
        public void set(Variable var) throws InterpreterException {
            setCalledWith = var;
        }

        @Override
        public Variable get(String name) {
            getCalledWith = name;
            return null;
        }

        @Override
        public Iterator<Variable> iterator() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    private class SubroutinesStub implements Subroutines {

        public Subroutine addCalledWith;
        public String getCalledWith;

        @Override
        public void add(Subroutine s) throws InterpreterException {
            addCalledWith = s;
        }

        @Override
        public Subroutine get(String name) throws InterpreterException {
            getCalledWith = name;
            return null;
        }

    }
    
    private class ValueStackStub extends ValueStack {

        public Value pushCalledWith;
        public boolean popCalled;
        private boolean peekCalled;
        
        @Override
        public void push(Value v) {
            pushCalledWith = v;
        }

        @Override
        public Value pop() throws InterpreterException {
            popCalled = true;
            return null;
        }
        
        @Override
        public Value peek() throws InterpreterException {
            peekCalled = true;
            return null;
        }
        
    }
    
    private class StubValue extends ValueBase {
        StubValue() {
            super("STUB");
        }
    }
}
