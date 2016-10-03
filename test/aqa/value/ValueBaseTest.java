/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class ValueBaseTest {
    
    private Value subject;
    
    private class StubValue extends ValueBase {
        
        StubValue() {
            super("stub");
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
        public int compare(Value other) {
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
        
    
    interface V {
        public boolean goForIt();
    }
    
    class VBase implements V {
        @Override
        public boolean goForIt() {
            return true;
        }
    }
    
    class VImpl extends VBase {
        @Override
        public boolean goForIt() {
            return false;
        }
    }
    
    @Test
    public void testIt() {
        V iface;
        VBase base;
        VImpl impl;
        
        impl = new VImpl();
        assertEquals(false, impl.goForIt());
        
        base = (VBase) impl;
        assertEquals(false, base.goForIt());
        
        iface = base;
        assertEquals(false, iface.goForIt());
    }
    
}
