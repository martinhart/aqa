/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import aqa.parser.InputProvider;
import aqa.parser.IgnoreInstructionListener;
import aqa.parser.InstructionListener;
import aqa.parser.OutputWriter;
import aqa.parser.Parser;
import aqa.tokenizer.Token;
import aqa.tokenizer.Tokenizer;
import java.io.Reader;

/**
 * This is the main public facing class used to run the interpreter.  As a client,
 * you shouldn't need to use any other objects defined herein.
 * 
 * @author martinhart
 */
public class Interpreter {

    private final Tokenizer tokenizer;
    private final Parser parser;

    /**
     * Create an interpreter that will listen for new instructions as they're
     * You need to call execute to actually run the interpreter.
     * 
     * @param reader the source code to execute
     * @param writer the object that writes the parser's OUTPUT and INSPECT data
     * @param inputProvider the object that provides USERINPUT
     * @param instructionListener the object that listens to progress.
     */
    public Interpreter(Reader reader, OutputWriter writer, InputProvider inputProvider,
            InstructionListener instructionListener) {
        this.tokenizer = new Tokenizer(reader);
        this.parser = new Parser(writer, inputProvider, instructionListener);
    }
    
    /**
     * Create a new instance that will execute without listening for new instructions
     * (i.e. just run the program).
     * @param reader the source code to execute
     * @param writer the object that writes the parser's OUTPUT and INSPECT data
     * @param inputProvider the object that provides USERINPUT
     */
    public Interpreter(Reader reader, OutputWriter writer, InputProvider inputProvider) {
        this(reader, writer, inputProvider, new IgnoreInstructionListener());
    }

    /**
     * Run the interpreter.
     * @throws InterpreterException if there is an issue with execution.
     */
    public void execute() throws InterpreterException {
        tokenizeAndPrepareParser();
        parse();
    }

    /**
     * Invoke the tokenizer and add tokens to the parser.
     * @throws InterpreterException if there are invalid tokens in the source
     */
    private void tokenizeAndPrepareParser() throws InterpreterException {
        for (Token token : tokenizer.tokenize()) {
            parser.addToken(token);
        }
    }

    /**
     * Cycle through the tokens executing instructions.
     * @throws InterpreterException if execution fails
     */
    private void parse() throws InterpreterException {
        parser.parse();
    }
}
