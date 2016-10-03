/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.tokenizer.Token;

/**
 *
 * @author martinhart
 */
public class ValueFactory {
    
    public static Value createInteger(Token token) {
        return (Value) new IntegerValue(Integer.parseInt(token.getName()));
    }
    
    public static Value createBoolean(Token token) {
        return (Value) new BooleanValue(token.getName().equals("TRUE"));
    }

    public static Value createReal(Token token) {
        return (Value) new RealValue(Double.parseDouble(token.getName()));
    }    
    
    public static Value createString(Token token) {
        String name = token.getName();
        return (Value) new StringValue(name.substring(1, name.length()-1));
    }

    static Value createVariableName(Token token) {
        return (Value) new StringValue(token.getName());
    }
}
