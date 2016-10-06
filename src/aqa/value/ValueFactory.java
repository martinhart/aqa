/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.tokenizer.Token;

/**
 * This factory class can be used to instantiate specific subtypes of Value
 * without knowing construction details.
 * 
 * @author martinhart
 */
public class ValueFactory {
    
    /**
     * Create a new IntegerValue
     * @param param the underlying value
     * @return the Value representing param
     */
    public static Value createInteger(int param) {
        return new IntegerValue(param);
    }
   
    /**
     * Create a new IntegerValue from given token
     * @param token contains the value of the integer
     * @return the IntegerValue
     */
    public static Value createInteger(Token token) {
        return createInteger(Integer.parseInt(token.getName()));
    }
    
    /**
     * Create a new BooleanValue
     * @param param the underlying value
     * @return the Value representing param
     */
    public static Value createBoolean(boolean param) {
        return new BooleanValue(param);
    }
   
    /**
     * Create a new BooleanValue from given token
     * @param token contains the value to use
     * @return the BooleanValue
     */
    public static Value createBoolean(Token token) {
        return createBoolean(token.getName().equals("TRUE"));
    }

    /**
     * Create a new RealValue
     * @param param the underlying value
     * @return the Value representing param
     */
    public static Value createReal(double param) {
        return new RealValue(param);
    }
   
    /**
     * Create a new RealValue from given token
     * @param token contains the value to use
     * @return the RealValue
     */
    public static Value createReal(Token token) {
        return createReal(Double.parseDouble(token.getName()));
    }    
    
    /**
     * Create a new StringValue
     * @param param the underlying value
     * @return the Value representing param
     */
    public static Value createString(String param) {
        return new StringValue(param);
    }
   
    /**
     * Create a new StringValue from given token, skipping the string literal delimiters.
     * @param token contains the value to use
     * @return the StringValue
     */
    public static Value createString(Token token) {
        String name = token.getName();
        return createString(name.substring(1, name.length()-1));
    }

    /**
     * Create a new StringValue from given token that represents the name of
     * a variable.
     * @param token contains the value to use
     * @return the StringValue
     */
    static Value createVariableName(Token token) {
        return (Value) new StringValue(token.getName());
    }
}
