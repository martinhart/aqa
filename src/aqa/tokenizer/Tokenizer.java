/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.InterpreterException;
import java.io.IOException;
import java.io.Reader;

/**
 * This class reads the source code and splits it up into tokens to be 
 * parsed.
 * 
 * @author martinhart
 */
public class Tokenizer {

    /**
     * The input source code
     */
    private final Reader fin;
    
    /**
     * Line number we are currently tokenizing
     */
    private int currentLine;
    
    /**
     * Line number of current token
     */
    private int tokenLine;
    
    /**
     * Character we're currently reading
     */
    private char character;
    
    /**
     * Buffer we're filling with characters until we hit a terminator
     */
    private String buffer;
    
    /**
     * The set of tokens we've created
     */
    private Tokens tokens;
    
    /**
     * Are we currently in the middle of a string literal?
     */
    private boolean insideStringLiteral;
    
    /**
     * The values that separate tokens.
     */
    private final char[] TERMINATORS = {'\n', '(', ')', ' ', '\t', '+', '-', '>', '<', '=', '*', ',', '[', ']', '/', '!'};

    /**
     * Create a new tokenizer ready to run.
     * @param reader provider of the source code
     */
    public Tokenizer(Reader reader) {
        this.fin = reader;
    }

    /**
     * Run through the source code and create some tokens from it
     * @return the created tokens
     * @throws InterpreterException if there are problems tokenizing the input data.
     * 
     * TODO: this method is too long and is difficult to read.  Break it down.
     */
    public Tokens tokenize() throws InterpreterException {

        initialise();
        while (thereIsMoreData()) {
            incrementLineNumberIfNewLine();
            if (isComment()) {
                skipLine();
            }
            else {
                if (isStringDelimiter()) {
                    handleQuotedString();
                }
                else {
                    if (isEndOfToken() && !insideStringLiteral) {
                        if (!buffer.isEmpty()) {
                            addToken();
                        }
                        if (!isSpace()) {
                            buffer = "" + character;
                            addToken();
                        }
                    }
                    else {
                        buffer += character;
                    }
                }
            }
        }
        
        if (!buffer.isEmpty()) {
            addToken();
        }
        
        return tokens;
    }        

    /**
     * Set up initial state
     */
    private void initialise() {
        currentLine = 1;
        tokenLine = 1;
        buffer = "";
        tokens = new Tokens();
        insideStringLiteral = false;
    }

    /**
     * @return true if there's more in the input stream
     * @throws InterpreterException if an error occurs
     */
    private boolean thereIsMoreData() throws InterpreterException {
        try {
            int i = fin.read();
            if (i != -1) {
                character = (char) i;
                return true;
            }
            return false;
        } catch (IOException ex) {
            throw new InterpreterException(currentLine, ex.getMessage());
        }
    }

    private void incrementLineNumberIfNewLine() {
        if (isNewLine()) {
            incrementLineNumber();
        }
    }
    
    private boolean isComment() {
        return (character == '#');
    }
    
    private void skipLine() throws InterpreterException {
        while (thereIsMoreData()) {
            if (isNewLine()) {
                incrementLineNumber();
                break;
            }
        }
    }
    
    private boolean isNewLine() {
        return (character == '\n');
    }
    
    private void incrementLineNumber() {
        ++currentLine;
        if (buffer.isEmpty()) {
            ++tokenLine;
        }
    }
    
    private boolean isStringDelimiter() {
        return (character == '\'');
    }
    
    private void handleQuotedString() throws InterpreterException {
        if (insideStringLiteral) { // end of string
            insideStringLiteral = false;
            buffer += character;
            addToken();
        }
        else {
            buffer += character;
            insideStringLiteral = true;
        }
    }
    
    private void addToken() throws InterpreterException {
        Token token = new Token(buffer, tokenLine);
        tokens.append(token);
        tokenLine = currentLine;
        buffer = "";
    }
    
    private boolean isEndOfToken() {
        for (int i=0; i < TERMINATORS.length; i++) {
            if (character == TERMINATORS[i]) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSpace() {
        return Character.isWhitespace(character);
    }
}
