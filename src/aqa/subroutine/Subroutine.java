/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.InterpreterException;
import aqa.tokenizer.Token;
import aqa.tokenizer.Tokens;
import java.util.ArrayList;

/**
 * This class contains information about a subroutine.  It is used by the parser
 * to maintain a table of defined subroutines that can be called.
 * 
 * @author martinhart
 */
public class Subroutine {
   
    /**
     * The name of this subroutine
     */
    private final String name;
    
    /**
     * The names of the parameters
     */
    private final ArrayList<String> parameters;
    
    /**
     * The tokens to execute when the subroutine is called.
     */
    private final Tokens tokens;

    /**
     * Create a new subroutine
     * @param name name of subroutine
     */
    public Subroutine(String name) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.tokens = new Tokens();
    }

    /**
     * @return the name of this subroutine
     */
    public String getName() {
        return name;
    }

    /**
     * Add a new parameter to the subroutine
     * @param name name of parameter
     */
    public void addParameter(String name) {
        parameters.add(name);
    }

    /**
     * Add a token to the body of this subroutine
     * @param t token to execute when subroutine is called
     */
    public void addToken(Token t) {
        tokens.append(t);
    }
    
    /**
     * @return the number of parameters that are expected by this subroutine
     */
    public int getNumberOfParameters() {
        return parameters.size();
    }
    
    /**
     * retrieve a parameter name at specified index
     * @param index the index of the parameter
     * @return the parameter name
     * @throws InterpreterException if invalid index 
     */
    public String getParameter(int index) throws InterpreterException {
        try {
            return parameters.get(index);
        }
        catch(IndexOutOfBoundsException e) {
            throw new InterpreterException("subroutine '" + name + "' invalid parameter index '" + index + "'");
        }
    }
    
    /**
     * @return the tokens that form the body of this subroutine
     */
    public Tokens getTokens() {
        return tokens;
    }
    
}
