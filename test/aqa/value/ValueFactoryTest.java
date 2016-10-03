/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.tokenizer.Token;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class ValueFactoryTest {
    
    public ValueFactoryTest() {
    }

    @Test
    public void testCreateInteger() throws Exception {
        assertEquals(true, ValueFactory.createInteger(new Token("1", 1)) instanceof IntegerValue);
    }
    
    @Test
    public void testCreateBoolean() throws Exception {
        assertEquals(true, ValueFactory.createBoolean(new Token("TRUE", 1)) instanceof BooleanValue);
    }
    
    @Test
    public void testCreateReal() throws Exception {
        assertEquals(true, ValueFactory.createReal(new Token("2.3", 1)) instanceof RealValue);
        
    }
    
    @Test
    public void testCreateString() throws Exception {
        assertEquals(true, ValueFactory.createString(new Token("'Hello'", 1)) instanceof StringValue);
    }
    
    @Test
    public void testCreateVariableName() throws Exception {
        Value v = ValueFactory.createVariableName(new Token("a", 1));
        assertEquals(true, v instanceof StringValue);
        StringValue sv = (StringValue) v;
        assertEquals("a", sv.value);
    }
    
}
