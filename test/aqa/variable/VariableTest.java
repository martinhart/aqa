/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.variable;

import aqa.InterpreterException;
import aqa.value.StringValue;
import aqa.value.IntegerValue;
import aqa.value.NullValue;
import aqa.value.ValueBase;
import aqa.value.Value;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class VariableTest {
    
    private Variable subject;
    
    @Before
    public void setUp() {
        subject = new Variable("Variable1");
    }
    
    @Test
    public void constructorSuppliesName() {
        assertEquals("Variable1", subject.getName());
    }
    
    @Test
    public void testConstructorSetsName() {
        subject = new Variable("wibble");
        assertEquals("wibble", subject.getName());
    }
    
    @Test
    public void whenCreatedItHasNullValue() {
        assertEquals(true, subject.getValue() instanceof NullValue);
    }
    
    @Test
    public void notConstantByDefault() {
        assertEquals(false, subject.isConstant());
    }
    
    @Test
    public void ifConstantCanStillAssignFirstValue() throws Exception {
        Value sv = new StubValue();
        subject.makeConstant();
        subject.setValue(sv);
        assertEquals(sv, subject.getValue());
    }
    
    @Test
    public void canChangeValueOfNonConstantVariable() throws Exception {
        Value v1 = new StubValue();
        Value v2 = new StubValue();
        subject.setValue(v1);
        subject.setValue(v2);
        assertEquals(v2, subject.getValue());
        assertNotEquals(v1, subject.getValue());
    }
    
    @Test(expected=Exception.class)
    public void cannotChangeValueOfConstantVariable() throws Exception {
        Value v1 = new StubValue();
        Value v2 = new StubValue();
        subject.makeConstant();
        subject.setValue(v1);
        subject.setValue(v2);
    }
    
    private class StubValue extends ValueBase {

        public StubValue() {
            super("StubValue");
        }

        @Override
        public String inspect() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String output() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int compare(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean equal(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Value add(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Value subtract(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Value multiply(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Value divide(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Value div(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Value mod(Value other) throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public IntegerValue length() throws InterpreterException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getValue() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
