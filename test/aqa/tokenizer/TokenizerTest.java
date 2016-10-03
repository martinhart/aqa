/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.tokenizer;

import aqa.tokenizer.Tokenizer;
import aqa.tokenizer.Tokens;
import aqa.tokenizer.Token;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinhart
 */
public class TokenizerTest {

    private Tokenizer subject;
    private Reader reader;
    private Tokens tokens;
    private Token token;

    @Test
    public void testTokenizingAnEmptyString() throws Exception {
        createSource("");
        tokens = subject.tokenize();
        assertEquals(true, tokens.isEmpty());
    }

    @Test
    public void testTokenizingASingleCharacter() throws Exception {
        createSource("A");
        tokens = subject.tokenize();
        assertEquals(false, tokens.isEmpty());
    }

    @Test
    public void testSeparators() throws Exception {
        createSource("()\n"
                + "1+2\n"
                + "3-4\n"
                + "1>2\n"
                + "2<1\n"
                + "1=2\n"
                + "hello*bye\n"
                + "a,b\n"
                + "a[3]\n"
                + "9/4");
        tokens = subject.tokenize();
        matchToken(0, "(", 1);
        matchToken(1, ")", 1);
        matchToken(2, "1", 2);
        matchToken(3, "+", 2);
        matchToken(4, "2", 2);
        matchToken(5, "3", 3);
        matchToken(6, "-", 3);
        matchToken(7, "4", 3);
        matchToken(8, "1", 4);
        matchToken(9, ">", 4);
        matchToken(10, "2", 4);
        matchToken(11, "2", 5);
        matchToken(12, "<", 5);
        matchToken(13, "1", 5);
        matchToken(14, "1", 6);
        matchToken(15, "=", 6);
        matchToken(16, "2", 6);
        matchToken(17, "hello", 7);
        matchToken(18, "*", 7);
        matchToken(19, "bye", 7);
        matchToken(20, "a", 8);
        matchToken(21, ",", 8);
        matchToken(22, "b", 8);
        matchToken(23, "a", 9);
        matchToken(24, "[", 9);
        matchToken(25, "3", 9);
        matchToken(26, "]", 9);
        matchToken(27, "9", 10);
        matchToken(28, "/", 10);
        matchToken(29, "4", 10);
    }

    @Test
    public void testSingleToken() throws Exception {
        createSource("hello");
        tokens = subject.tokenize();
        matchToken(0, "hello", 1);
    }

    @Test
    public void testWhitespace() throws Exception {
        createSource("this is 4	\t		tokens\n"
                + "and three\n"
                + "more   ");
        tokens = subject.tokenize();
        matchToken(0, "this", 1);
        matchToken(1, "is", 1);
        matchToken(2, "4", 1);
        matchToken(3, "tokens", 1);
        matchToken(4, "and", 2);
        matchToken(5, "three", 2);
        matchToken(6, "more", 3);
    }
    
    @Test
    public void testQuotedString() throws Exception {
        createSource("'Hello, world'");
        tokens = subject.tokenize();
        matchToken(0, "'Hello, world'", 1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testIgnoresTrailingWhitespace() throws Exception {
        createSource("a      ");
        tokens = subject.tokenize();
        token = tokens.getToken(1);
    }

    private void createSource(String data) {
        reader = new StringReader(data);
        subject = new Tokenizer(reader);
    }

    private void matchToken(int index, String name, int line) {
        token = tokens.getToken(index);
        assertEquals(name, token.getName());
        assertEquals(line, token.getLineNumber());
    }

}
