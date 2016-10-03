/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.tokenizer.Token;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author martinhart
 */
public class ParserStringsTest {
    private Parser subject;
    
    @Before
    public void setup() {
        subject = new Parser();
    }
    
    @Test
    public void testStringLength() throws InterpreterException {
        addTokens("LEN", "(", "'Hello'", ")");
        parse();
        assertEquals(5, subject.getVM().getValueStack().popInteger());
    }
    
    private void addTokens(String... tokens) {
        for (String token : tokens) {
            try {
                subject.addToken(new Token(token, 1));
            }catch (InterpreterException ex) {
                Logger.getLogger(ParserStringsTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void parse() {
        try {
            subject.parse();
        } catch (InterpreterException ex) {
            Logger.getLogger(ParserStringsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
