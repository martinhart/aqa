/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.InterpreterException;

/**
 *
 * @author martinhart
 */
public class TokenSequencer {

    private final Tokens tokens;
    private int currentIndex;
    private Token currentToken;

    public TokenSequencer(Tokens tokens) {
        this.tokens = tokens;
        currentIndex = 0;
        getToken();
    }

    public boolean thereIsAToken() {
        return (currentToken != null);
    }

    public void advance() {
        ++currentIndex;
        getToken();
    }

    public boolean match(String value) {
        if (currentToken != null) {
            return (value.equals(currentToken.getName()));
        }
        return false;
    }

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

    public String getCurrentTokenName() {
        if (currentToken == null) {
            return "";
        }
        return currentToken.getName();
    }

    public boolean contains(String value) {
        if (currentToken == null) {
            return false;
        }
        return getCurrentTokenName().contains(value);
    }

    public void jumpToPosition(int i) {
        if (i < 0) {
            currentIndex = 0;
        } else {
            currentIndex = i;
        }
        getToken();
    }

    public void retreat() {
        if (currentIndex > 0) {
            --currentIndex;
        }
        getToken();
    }

    public int getCurrentTokenLine() {
        if (currentToken == null) {
            return 1;
        }
        return currentToken.getLineNumber();
    }

    public Token getCurrentToken() throws InterpreterException {
        if (currentToken == null) {
            return new Token("<void>", 1);
        }
        return currentToken;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    private void getToken() {
        try {
            currentToken = tokens.getToken(currentIndex);
        } catch (IndexOutOfBoundsException e) {
            currentToken = null;
        }
    }

}
