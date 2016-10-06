/*
 * Copyright (c) 2016 Martin Hart.  Under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.InterpreterException;

/**
 * This class represents a single token to be parsed and executed.  Tokens
 * are created from the source code and are associated with a line number from
 * that code.
 * 
 * @author martinhart
 */
public class Token {
   
    /**
     * The token name (e.g. OUTPUT)
     */
    private String name;
    
    /**
     * The line in the source code on which this token was present
     */
    private final int lineNumber;

    /**
     * Create a new token.
     * @param name the token value - must not be empty
     * @param line the line token occurs on (must be >=0)
     * @throws InterpreterException upon invalid arguments
     */
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

    /**
     * @return the line in the source code that this token can be found
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return the value of this token
     */
    public String getName() {
        return name;
    }

    /**
     * Change the name of this token.
     * 
     * For example, when parsing that integer literal '-1', the tokenizer might
     * see this as two separate tokens.  When parsing, we can reconstruct the
     * original token value according to context:
     * 
     * 1 -1     -> should be parsed as a subtract
     * -1       -> should be parsed as an integer literal
     * 
     * @param name new token name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
