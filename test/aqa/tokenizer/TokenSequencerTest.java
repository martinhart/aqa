/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.InterpreterException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author martinhart
 */
public class TokenSequencerTest {
    
    private TokenSequencer subject;
    private Tokens tokens;
    
    @Before
    public void setUp() {
        tokens = new Tokens();
    }
    
    @Test
    public void testCurrentIndexIs0OnCreation() {
        addTokens("1");
        createSubject();
        assertEquals(0, subject.getCurrentIndex());
    }
    
    @Test
    public void testCurrentIndexAfterAdvance() {
        addTokens("1", "2");
        createSubject();
        subject.advance();
        assertEquals(1, subject.getCurrentIndex());
    }
    
    @Test
    public void testCurrentIndexAfterRetreat() {
        addTokens("1", "2");
        createSubject();
        subject.advance();
        subject.retreat();
        assertEquals(0, subject.getCurrentIndex());        
    }
    
    @Test
    public void testCurrentIndexAfterJump() {
        addTokens("1", "2", "3", "4");
        createSubject();
        subject.jumpToPosition(3);
        assertEquals(3, subject.getCurrentIndex());        
        
    }
    
    @Test
    public void testThereIsATokenWhenEmpty() {
        createSubject();
        assertEquals(false, subject.thereIsAToken());
    }
    
    @Test
    public void testThereIsATokenWhenFull() {
        addTokens("hello");
        createSubject();
        assertEquals(true, subject.thereIsAToken());
    }
    
    @Test
    public void testThereIsATokenWhenAdvancingAndStillFull() {
        addTokens("hello", "bye");
        createSubject();
        subject.advance();
        assertEquals(true, subject.thereIsAToken());
    }
    
    @Test
    public void testThereIsATokenWhenAdvancingAndNoMoreExist() {
        addTokens("hello");
        createSubject();
        subject.advance();
        assertEquals(false, subject.thereIsAToken());
    }

    @Test
    public void testMatchWithoutTokenShouldNotMatch() {
        createSubject();
        assertEquals(false, subject.match("hi"));
    }
    
    @Test
    public void testMatchWhenNotMatching() {
        addTokens("hello");
        createSubject();
        assertEquals(false, subject.match("help"));
    }
    
    @Test
    public void testMatchWhenMatching() {
        addTokens("hello");
        createSubject();
        assertEquals(true, subject.match("hello"));
    }
    
    @Test
    public void testMatchWithManyTokens() {
        addTokens("1", "2", "3");
        createSubject();
        assertEquals(false, subject.match("2"));
        subject.advance();
        assertEquals(true, subject.match("2"));
        subject.advance();
        assertEquals(false, subject.match("2"));
    }
    
    @Test(expected=InterpreterException.class)
    public void testExpectWhenNotThere() throws InterpreterException {
        addTokens("a");
        createSubject();
        subject.expect("b");
    }
    
    @Test
    public void testExpectWhenMatching() throws InterpreterException {
        addTokens("a", "b");
        createSubject();
        assertEquals(true, subject.expect("a"));
        assertEquals("b", subject.getCurrentTokenName());
    }
    
    @Test
    public void testContainsWhenNotContaining() {
        addTokens("123");
        createSubject();
        assertEquals(false, subject.contains("."));
    }
    
    @Test
    public void testContainsWhenContaining() {
        addTokens("12.3");
        createSubject();
        assertEquals(true, subject.contains("."));
    }
    
    @Test
    public void testAdvance() {
        addTokens("1", "2");
        createSubject();
        assertEquals("1", subject.getCurrentTokenName());
        subject.advance();
        assertEquals("2", subject.getCurrentTokenName());
        subject.advance();
        assertEquals("", subject.getCurrentTokenName());
    }
    
    @Test
    public void testRetreat() {
        addTokens("1", "2", "3");
        createSubject();
        subject.jumpToPosition(2);
        assertEquals("3", subject.getCurrentTokenName());
        subject.retreat();
        assertEquals("2", subject.getCurrentTokenName());
        subject.retreat();
        assertEquals("1", subject.getCurrentTokenName());
        subject.retreat();
        assertEquals("1", subject.getCurrentTokenName());
    }
    
    @Test
    public void testJumpBeforeStartStaysOnStart() {
        addTokens("1");
        createSubject();
        subject.jumpToPosition(-1);
        assertEquals("1", subject.getCurrentTokenName());
    }
    
    @Test
    public void testJumpAfterEndReturnsEmptyToken() {
        addTokens("1");
        createSubject();
        subject.jumpToPosition(100);
        assertEquals("", subject.getCurrentTokenName());
    }
    
    @Test
    public void testJumpToCorrectIndex() {
        addTokens("1", "2", "3");
        createSubject();
        subject.jumpToPosition(1);
        assertEquals("2", subject.getCurrentTokenName());
        subject.jumpToPosition(2);
        assertEquals("3", subject.getCurrentTokenName());
    }
    
    @Test
    public void testGetCurrentTokenLine() {
        addTokens("1", "2");
        createSubject();
        assertEquals(1, subject.getCurrentTokenLine());
        subject.advance();
        assertEquals(2, subject.getCurrentTokenLine());
    }
    
    @Test
    public void testGetLineWithoutAValidTokenDefaultsToFirstLine() {
        createSubject();
        assertEquals(1, subject.getCurrentTokenLine());
    }
    
    @Test
    public void testGetCurrentTokenWithoutAValidToken() throws InterpreterException {
        Token t;
        createSubject();
        t = subject.getCurrentToken();
        assertNotNull(t);
        
        addTokens("1");
        createSubject();
        subject.advance();
        t = subject.getCurrentToken();
        assertNotNull(t);
    }
    
    @Test
    public void testGetCurrentTokenWithAToken() throws InterpreterException {
        Token a, b;
        addTokens("a", "b");
        createSubject();
        a = subject.getCurrentToken();
        subject.advance();
        b = subject.getCurrentToken();
        assertNotEquals(a, b);
    }

    private void createSubject() {
        subject = new TokenSequencer(tokens);
    }
    
    private void addTokens(String... tokenNames) {
        for (int i=0; i<tokenNames.length; i++) {
            try {
                tokens.append(new Token(tokenNames[i], i+1));
            } catch (InterpreterException ex) {
                Logger.getLogger(TokenSequencerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
