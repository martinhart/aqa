/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.InterpreterException;

/**
 * This class is responsible for providing information about the current token
 * being executed and a means to navigate between tokens.
 *
 * @author martinhart
 */
public class TokenSequencer {

    /**
     * The tokens managed by this object.
     */
    private final Tokens tokens;

    /**
     * The index of the token we're currently working on.
     */
    private int currentIndex;

    /**
     * The token we're currently working on.
     */
    private Token currentToken;

    /**
     * Create a new token sequencer
     *
     * @param tokens the tokens to manage
     */
    public TokenSequencer(Tokens tokens) {
        this.tokens = tokens;
        currentIndex = 0;
        getToken();
    }

    /**
     * Check if there is a token to do anything with.
     *
     * @return true if we are currently processing a token, false if we have
     * reached the end of the tokens.
     */
    public boolean thereIsAToken() {
        return (currentToken != null);
    }

    /**
     * Move on to next token.
     */
    public void advance() {
        ++currentIndex;
        getToken();
    }

    /**
     * Check if the current token matches an argument.  e.g.
     * 
     * if (tokenSequencer.match("+")) {
     *  // do add logic
     * }
     * 
     * @param value the value to match with
     * @return true if the value of the curent token matches argument.  false
     * if not, or if there is no current token.
     */
    public boolean match(String value) {
        if (currentToken != null) {
            return (value.equals(currentToken.getName()));
        }
        return false;
    }

    /**
     * Require the current token to match an argument.  e.g.
     * 
     * tokenSequencer.expect("ENDIF")
     * // this line will not be executed unless ENDIF was encountered.
     * 
     * @param value the value to match with
     * @return true
     * @throws InterpreterException if there is no match or if there is no
     * current token.
     */
    public boolean expect(String value) throws InterpreterException {
        if (!match(value)) {
            int lineNumber = 1;
            String message = "expected '" + value + "'";
            if (currentToken != null) {
                lineNumber = currentToken.getLineNumber();
            }
            throw new InterpreterException(lineNumber, message);
        }
        advance();
        return true;
    }

    /**
     * Return the name of the current token
     * @return the current token name or an empty string if there is no token.
     */
    public String getCurrentTokenName() {
        if (currentToken == null) {
            return "";
        }
        return currentToken.getName();
    }

    /**
     * Check if the current token contains the value specified.  e.g.
     * 
     * if (tokenSequencer.contains(".")) {
     *  // process real number
     * }
     * else {
     *  // process integer
     * }
     * 
     * @param value the value to check
     * @return true if value contained within token, false if not or if there
     * is no current token.
     */
    public boolean contains(String value) {
        if (currentToken == null) {
            return false;
        }
        return getCurrentTokenName().contains(value);
    }

    /**
     * Change the current token.
     * @param i the index of the new current token.
     */
    public void jumpToPosition(int i) {
        if (i < 0) {
            currentIndex = 0;
        } else {
            currentIndex = i;
        }
        getToken();
    }

    /**
     * Move back to the previous token
     */
    public void retreat() {
        if (currentIndex > 0) {
            --currentIndex;
        }
        getToken();
    }

    /**
     * @return the line number of the current token
     */
    public int getCurrentTokenLine() {
        if (currentToken == null) {
            return 1;
        }
        return currentToken.getLineNumber();
    }

    /**
     * @return the current token
     * @throws InterpreterException if an error occurs
     * 
     * TODO: the exception is thrown because we invent a token if there isn't one.
     * It's the token constructor that throws the exception.  This is messy.  It
     * would be better to construct a singleton token and return that, then there
     * will be no exception to raise.
     */
    public Token getCurrentToken() throws InterpreterException {
        if (currentToken == null) {
            return new Token("<void>", 1);
        }
        return currentToken;
    }

    /**
     * @return the index into tokens of the current token
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Set the currentToken based on currentIndex.
     */
    private void getToken() {
        try {
            currentToken = tokens.getToken(currentIndex);
        } catch (IndexOutOfBoundsException e) {
            currentToken = null;
        }
    }
}
