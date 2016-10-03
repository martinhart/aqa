/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;
import java.util.ArrayList;

/**
 *
 * @author martinhart
 */
public class ArrayValue extends ValueBase {
    
    //private final HashMap<Integer, Value> values;
    private final ArrayList<Value> values;
    
    public ArrayValue() {
        super("array");
        //values = new HashMap<>();
        values = new ArrayList<>();
    }
    
    @Override
    public String output() throws InterpreterException {
        String s = "";
        for (int i=0; i<values.size(); i++) {
            s += values.get(i).output();
        }
        return s;
    }
    
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
