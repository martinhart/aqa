/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import aqa.parser.InputProvider;
import aqa.parser.OutputWriter;
import aqa.parser.Parser;
import aqa.InterpreterException;
import aqa.tokenizer.Token;
import aqa.tokenizer.Tokenizer;
import java.io.Reader;

/**
 *
 * @author martinhart
 */
public class Interpreter {

    private final Tokenizer tokenizer;
    private final Parser parser;

    /**
     *
     * @param reader the source file to execute
     * @param writer the object that writes the parser's OUTPUT and INSPECT data
     * @param inputProvider the object that provides USERINPUT
     */
    public Interpreter(Reader reader, OutputWriter writer, InputProvider inputProvider) {
        this.tokenizer = new Tokenizer(reader);
        this.parser = new Parser(writer, inputProvider);
    }

    public void execute() throws InterpreterException {
        tokenize();
        parse();
    }

    private void tokenize() throws InterpreterException {
        for (Token token : tokenizer.tokenize()) {
            parser.addToken(token);
        }
    }

    private void parse() throws InterpreterException {
        parser.parse();
    }
}
