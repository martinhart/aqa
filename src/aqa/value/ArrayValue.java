/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;
import java.util.ArrayList;

/**
 * This class represents an array.
 * 
 * @author martinhart
 */
public class ArrayValue extends ValueBase {

    /**
     * The values contained inside this array
     */
    private final ArrayList<Value> values;
    
    public ArrayValue() {
        super("array");
        values = new ArrayList<>();
    }
    
    /**
     * AQA do not define what should happen with OUTPUT ary so I've chosen the
     * Ruby style - to just print the values one after the other.
     * @return the output data
     * @throws InterpreterException This override should not throw an exception.
     */
    @Override
    public String output() throws InterpreterException {
        String s = "";
        for (int i=0; i<values.size(); i++) {
            s += values.get(i).output();
        }
        return s;
    }
    
    /**
     * AQA do not include INSPECT in the language.  It's added to aid debugging.
     * @return the output data
     * @throws InterpreterException - should never happen
     */
    @Override
    public String inspect() throws InterpreterException {
        String s = "[";
        for (int i=0; i<values.size(); i++) {
            s += values.get(i).inspect();
            if (i+1 < values.size()) {
                s += ", ";
            }
        }
        return s + "]";
    }

    @Override
    public void setAtIndex(int index, Value value) throws InterpreterException {
        while(index >= values.size()) {
            values.add(NullValue.instance);
        }
        values.set(index, value);
    }
    
    @Override
    public Value getAtIndex(int index) throws InterpreterException {
        while(index >= values.size()) {
            values.add(NullValue.instance);
        }
        return values.get(index);
    }
    
    @Override
    public IntegerValue length() throws InterpreterException {        
        return new IntegerValue(values.size());
    }
 
    @Override
    public Value makeCopy() throws InterpreterException {
        ArrayValue av = new ArrayValue();
        for (int i=0; i<values.size(); i++) {
            av.setAtIndex(i, values.get(i).makeCopy());
        }
        return av;
    }
}
