/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.InterpreterException;
import aqa.tokenizer.Token;
import aqa.tokenizer.Tokens;
import java.util.ArrayList;

/**
 *
 * @author martinhart
 */
public class Subroutine {
    
    private final String name;
    private final ArrayList<String> parameters;
    private final Tokens tokens;

    public Subroutine(String name) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.tokens = new Tokens();
    }

    public String getName() {
        return name;
    }

    public void addParameter(String name) {
        parameters.add(name);
    }

    public void addToken(Token t) {
        tokens.append(t);
    }
    
    /**
     * retrieve the number of parameters that are expected by this subroutine
     * @return number of parameters
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
    
    public Tokens getTokens() {
        return tokens;
    }
    
}
