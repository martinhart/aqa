/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

/**
 *
 * @author martinhart
 */
public class NullValue extends ValueBase {
    
    public static NullValue instance = new NullValue();
    
    private NullValue() {
        super("null");
    }
    
    public String inspect() {
        return "<NULL>";
    }
}
