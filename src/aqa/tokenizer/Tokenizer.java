/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.InterpreterException;
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author martinhart
 */
public class Tokenizer {

    private final Reader fin;
    private int currentLine;
    private int tokenLine;
    private char character;
    private String buffer;
    private Tokens tokens;
    private boolean insideStringLiteral;
    private final char[] TOKENS = {'\n', '(', ')', ' ', '\t', '+', '-', '>', '<', '=', '*', ',', '[', ']', '/', '!'};

    public Tokenizer(Reader reader) {
        this.fin = reader;
    }

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

    private void initialise() {
        currentLine = 1;
        tokenLine = 1;
        buffer = "";
        tokens = new Tokens();
        insideStringLiteral = false;
    }

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
        for (int i=0; i < TOKENS.length; i++) {
            if (character == TOKENS[i]) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSpace() {
        return Character.isWhitespace(character);
    }
}
