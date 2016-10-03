/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import aqa.parser.InputProvider;
import aqa.parser.OutputWriter;
import aqa.parser.Parser;
import aqa.tokenizer.Token;
import aqa.tokenizer.Tokenizer;
import java.io.Reader;

/**
 * This is the main public facing class used to run the interpreter.  As a client,
 * you shouldn't need to use any other objects defined herein.
 * @author martinhart
 */
public class Interpreter {

    private final Tokenizer tokenizer;
    private final Parser parser;

    /**
     * Create a new instance.  You need to call execute to actually run the
     * interpreter.
     * @param reader the source file to execute
     * @param writer the object that writes the parser's OUTPUT and INSPECT data
     * @param inputProvider the object that provides USERINPUT
     */
    public Interpreter(Reader reader, OutputWriter writer, InputProvider inputProvider) {
        this.tokenizer = new Tokenizer(reader);
        this.parser = new Parser(writer, inputProvider);
    }

    /**
     * Run the interpreter.
     * @throws InterpreterException if there is an issue.
     */
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
