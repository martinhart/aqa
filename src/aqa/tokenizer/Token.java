/*
 * Copyright (c) 2016 Martin Hart.  Under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.InterpreterException;

/**
 *
 * @author martinhart
 */
public class Token {
    
    private String name;
    private final int lineNumber;

    public Token(String name, int line) throws InterpreterException {
        if (line < 1) {
            throw new InterpreterException(line, "Invalid line number");
        }
        
        if (name.isEmpty()) {
            throw new InterpreterException(line, "Invalid token name");
        }
        
        this.name = name;
        this.lineNumber = line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
