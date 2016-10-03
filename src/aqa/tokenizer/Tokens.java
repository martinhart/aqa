/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author martinhart
 */
public class Tokens implements Iterable<Token> {
    
    private final ArrayList<Token> tokens;

    public Tokens() {
        this.tokens = new ArrayList<>();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    public void append(Token token) {
        // we can ignore the result of add because it must return true
        // see Collections.add documentation.
        boolean ignore = tokens.add(token);
    }

    public Token getToken(int index) {
        return tokens.get(index);
    }

    public int size() {
        return tokens.size();
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }
}
