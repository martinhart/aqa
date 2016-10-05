/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import aqa.parser.InputProvider;
import aqa.parser.OutputWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Run through all of the files in the examples folder and ensure that
 * the system doesn't fail!
 * @author martinhart
 */
public class AcceptanceTest {
    
    private Interpreter subject;
    private OutputObserverStub outputObserver;
    private InputProviderStub inputProvider;
    private String codeToParse;

    @Before
    public void setUp() {
        outputObserver = new OutputObserverStub();
        inputProvider = new InputProviderStub();
        codeToParse = "";
    }

    @Test
    public void testExamples() throws InterpreterException, FileNotFoundException {
        File folder = new File("examples");
        File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles) {
            testExample(listOfFile);
        }
    }
    
    private void testExample(File path) throws FileNotFoundException, InterpreterException {        
        subject = new Interpreter(new FileReader(path), outputObserver, inputProvider);
        subject.execute();        
    }

    private class OutputObserverStub implements OutputWriter {

        public String message = "";

        @Override
        public void output(String message) {
            this.message = message;
        }
    }

    private class InputProviderStub implements InputProvider {

        String inputData;

        InputProviderStub() {
            inputData = "HELLO";
        }

        @Override
        public String getInput() {
            return inputData;
        }

    }    
}
