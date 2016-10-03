/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import aqa.parser.OutputWriter;
import aqa.parser.InputProvider;

/**
 *
 * @author martinhart
 */
public class InterpreterTest {

    private Interpreter subject;
    private OutputObserverStub outputObserver;
    private InputProviderStub inputProvider;
    private String codeToParse;
    private String inputData;

    @Before
    public void setUp() {
        outputObserver = new OutputObserverStub();
        inputProvider = new InputProviderStub();
        codeToParse = "";
        inputData = "";
    }

    @Test
    public void testEmpty() throws Exception {
        parse("");
        check("");
    }

    @Test
    public void literals() throws Exception {
        parseAndCheck("OUTPUT 1234", "1234");
        parseAndCheck("OUTPUT TRUE", "TRUE");
        parseAndCheck("OUTPUT FALSE", "FALSE");
        parseAndCheck("OUTPUT 3.14159", "3.14159");
        parseAndCheck("OUTPUT 'Hello, World!'", "Hello, World!");
        parseAndCheck("OUTPUT([1, 2])", "12");
    }

    @Test
    public void negativeInteger() throws Exception {
        parseAndCheck("OUTPUT -1", "-1");
    }

    @Test
    public void testAddition() throws Exception {
        parseAndCheck("OUTPUT (1 + 3)", "4");
        parseAndCheck("OUTPUT (20 + 2 + 4)", "26");
        parseAndCheck("OUTPUT (1 + 3.59)", "4.59");
        parseAndCheck("OUTPUT (2.2 + 30)", "32.2");
        parseAndCheck("OUTPUT (1.1 + 3.3)", "4.4");
        parseAndCheck("OUTPUT ('hello' + 'world')", "helloworld");
    }

    @Test
    public void testOutput() throws Exception {
        parseAndCheck("OUTPUT 23", "23");
        parseAndCheck("OUTPUT 23.45", "23.45");
        parseAndCheck("OUTPUT TRUE", "TRUE");
        parseAndCheck("OUTPUT 'Hi'", "Hi");
    }

    @Test
    public void testSubtraction() throws Exception {
        parseAndCheck("OUTPUT (10 - 3)", "7");
        parseAndCheck("OUTPUT (10.0 - 3.2)", "6.8");
        parseAndCheck("OUTPUT (10 - 0.1)", "9.9");
        parseAndCheck("OUTPUT (9.9 - 2)", "7.9");
    }

    @Test
    public void testMultiplication() throws Exception {
        parseAndCheck("OUTPUT (10 * 3)", "30");
        parseAndCheck("OUTPUT (10.1 * 2.0)", "20.2");
        parseAndCheck("OUTPUT (10 * 2.2)", "22.0");
        parseAndCheck("OUTPUT (0.1 * 20)", "2.0");
    }

    @Test
    public void testIntegerDivision() throws Exception {
        parseAndCheck("OUTPUT (9 DIV 5)", "1");
        parseAndCheck("OUTPUT (5 DIV 2)", "2");
        parseAndCheck("OUTPUT (8 DIV 4)", "2");
    }

    @Test
    public void testIntegerDivisionResultingInRealWithDiv() throws Exception {
        parse("OUTPUT (10 DIV 3)");
        check("3");
    }

    @Test
    public void testIntegerDivisionResultingInRealWithDivide() throws Exception {
        parse("OUTPUT (10 / 4)");
        check("2.5");
    }

    @Test
    public void testIntegerModulus() throws Exception {
        parseAndCheck("OUTPUT (9 MOD 5)", "4");
        parseAndCheck("OUTPUT (5 MOD 2)", "1");
        parseAndCheck("OUTPUT (8 MOD 4)", "0");
    }

    @Test
    public void testDivision() throws Exception {
        parseAndCheck("OUTPUT (10.0 / 2.5)", "4.0");
        parseAndCheck("OUTPUT (10.0 / 2)", "5.0");
        parseAndCheck("OUTPUT (100 / 2.5)", "40.0");
    }

    @Test
    public void greaterThan() throws Exception {
        parseAndCheck("OUTPUT (100 > 20)", "TRUE");
        parseAndCheck("OUTPUT (100 > 200)", "FALSE");
        parseAndCheck("OUTPUT (100.3 > 100.2)", "TRUE");
        parseAndCheck("OUTPUT (100.3 > 100.4)", "FALSE");
        parseAndCheck("OUTPUT (100 > 99.9)", "TRUE");
        parseAndCheck("OUTPUT (100 > 999.99)", "FALSE");
        parseAndCheck("OUTPUT (99.9 > 100)", "FALSE");
        parseAndCheck("OUTPUT (99.9 > 99)", "TRUE");
    }

    @Test
    public void greaterThanOrEqual() throws Exception {
        parseAndCheck("OUTPUT (100 >= 20)", "TRUE");
        parseAndCheck("OUTPUT (100 >= 200)", "FALSE");
        parseAndCheck("OUTPUT (100.3 >= 100.2)", "TRUE");
        parseAndCheck("OUTPUT (100.3 >= 100.4)", "FALSE");
        parseAndCheck("OUTPUT (100 >= 99.9)", "TRUE");
        parseAndCheck("OUTPUT (100 >= 999.99)", "FALSE");
        parseAndCheck("OUTPUT (99.9 >= 100)", "FALSE");
        parseAndCheck("OUTPUT (99.9 >= 99)", "TRUE");
        parseAndCheck("OUTPUT (99.9 >= 99.9)", "TRUE");
    }

    @Test
    public void lessThan() throws Exception {
        parseAndCheck("9 < 8", "");
        parseAndCheck("OUTPUT (99 < 98)", "FALSE");
        parseAndCheck("OUTPUT (98 < 99)", "TRUE");
        parseAndCheck("OUTPUT (100.3 < 100.2)", "FALSE");
        parseAndCheck("OUTPUT (100.3 < 100.4)", "TRUE");
        parseAndCheck("OUTPUT (100.3 < 100)", "FALSE");
        parseAndCheck("OUTPUT (99.3 < 100)", "TRUE");
        parseAndCheck("OUTPUT (100 < 99.3)", "FALSE");
        parseAndCheck("OUTPUT (100 < 101.3)", "TRUE");
    }

    @Test
    public void lessThanOrEqual() throws Exception {
        parseAndCheck("OUTPUT (99 <= 98)", "FALSE");
        parseAndCheck("OUTPUT (98 <= 99)", "TRUE");
        parseAndCheck("OUTPUT (98 <= 98)", "TRUE");
        parseAndCheck("OUTPUT (100.3 <= 100.2)", "FALSE");
        parseAndCheck("OUTPUT (100.3 <= 100.4)", "TRUE");
        parseAndCheck("OUTPUT (100.3 <= 100.3)", "TRUE");
        parseAndCheck("OUTPUT (100.3 <= 100)", "FALSE");
        parseAndCheck("OUTPUT (99.3 <= 100)", "TRUE");
        parseAndCheck("OUTPUT (99.3 <= 99.3)", "TRUE");
        parseAndCheck("OUTPUT (100 <= 99.3)", "FALSE");
        parseAndCheck("OUTPUT (100 <= 101.3)", "TRUE");
    }

    @Test
    public void equal() throws Exception {
        parseAndCheck("OUTPUT (19 = 19)", "TRUE");
        parseAndCheck("OUTPUT (19 = 18)", "FALSE");
        parseAndCheck("OUTPUT (1.1 = 1.1)", "TRUE");
        parseAndCheck("OUTPUT (1.1 = 1.2)", "FALSE");
        parseAndCheck("OUTPUT (TRUE = TRUE)", "TRUE");
        parseAndCheck("OUTPUT (TRUE = FALSE)", "FALSE");
        parseAndCheck("OUTPUT ('a' = 'a')", "TRUE");
        parseAndCheck("OUTPUT ('a' = 'b')", "FALSE");
    }

    @Test
    public void notEqual() throws Exception {
        parseAndCheck("OUTPUT (19 != 19)", "FALSE");
        parseAndCheck("OUTPUT (19 != 18)", "TRUE");
        parseAndCheck("OUTPUT (1.1 != 1.1)", "FALSE");
        parseAndCheck("OUTPUT (1.1 != 1.2)", "TRUE");
        parseAndCheck("OUTPUT (TRUE != TRUE)", "FALSE");
        parseAndCheck("OUTPUT (TRUE != FALSE)", "TRUE");
        parseAndCheck("OUTPUT ('a' != 'a')", "FALSE");
        parseAndCheck("OUTPUT ('a' != 'b')", "TRUE");
    }

    @Test
    public void logicalAnd() throws Exception {
        parseAndCheck("OUTPUT ((3 = 3) AND (3 <= 4))", "TRUE");
        parseAndCheck("OUTPUT ((3 != 3) AND (3 <= 4))", "FALSE");
    }

    @Test
    public void logicalOr() throws Exception {
        parseAndCheck("OUTPUT ((1 < 1) OR (1 < 2))", "TRUE");
        parseAndCheck("OUTPUT ((1 < 1) OR (1 <= 0))", "FALSE");
        parseAndCheck("OUTPUT ((1 < 2) OR (2 < 3))", "TRUE");
    }

    @Test
    public void logicalNot() throws Exception {
        parseAndCheck("OUTPUT (NOT (1=1))", "FALSE");
        parseAndCheck("OUTPUT (NOT (1=2))", "TRUE");
    }

    @Test
    public void variableDeclaration() throws Exception {
        parseAndCheck("a <- 10 OUTPUT a", "10");
    }

    @Test
    public void variableAssignment() throws Exception {
        push("a <- 1");
        push("b <- 2");
        push("a <- a + b");
        push("OUTPUT a");
        parse();
        check("3");
    }

    @Test
    public void stringLength() throws Exception {
        parseAndCheck("OUTPUT LEN('Hello')", "5");
        parseAndCheck("OUTPUT LEN('a')", "1");
    }

    @Test
    public void stringPosition() throws Exception {
        parseAndCheck("OUTPUT POSITION('computer science', 'm')", "2");
        parseAndCheck("OUTPUT POSITION('wibble', 'x')", "-1");
        parseAndCheck("OUTPUT POSITION('wibble', 'b')", "2");
    }

    @Test
    public void substring() throws Exception {
        parseAndCheck("OUTPUT SUBSTRING(2, 9, 'computer science')", "mputer s");
    }

    @Test
    public void stringToInt() throws Exception {
        parseAndCheck("OUTPUT STRING_TO_INT('19')", "19");
    }
    
    @Test(expected=InterpreterException.class)
    public void stringToIntWithInvalidData() throws Exception {
        parseAndCheck("OUTPUT STRING_TO_INT('1abacus9')", "NOTUSED");
    }

    @Test
    public void stringToReal() throws Exception {
        parseAndCheck("OUTPUT STRING_TO_REAL('19.2')", "19.2");
    }
    
    @Test(expected=InterpreterException.class)
    public void stringToRealWithInvalidData() throws Exception {
        parseAndCheck("OUTPUT STRING_TO_REAL('hek')", "NOTUSED");
    }

    @Test
    public void intToString() throws Exception {
        parseAndCheck("OUTPUT INT_TO_STRING(23) + 'a'", "23a");
    }

    @Test
    public void realToString() throws Exception {
        parseAndCheck("OUTPUT REAL_TO_STRING(1.5) + 'a'", "1.5a");
    }

    @Test
    public void charToCode() throws Exception {
        parseAndCheck("OUTPUT CHAR_TO_CODE('a')", "97");
    }

    @Test
    public void codeToChar() throws Exception {
        parseAndCheck("OUTPUT CODE_TO_CHAR(97)", "a");
    }
    
    @Test
    public void randomInt() throws Exception {
        parseAndCheck("OUTPUT RANDOM_INT(1, 1)", "1");
    }

    @Test
    public void arrayIndexing() throws Exception {
        push("a <- [1,2,3]");
        push("b <- a[1]");
        push("OUTPUT b");
        parse();
        check("2");
    }
    
    @Test(expected=InterpreterException.class)
    public void arrayIndexingUsingNonIntegerIndex() throws Exception {
        push("a <- 'hello'");
        push("b <- [1]");
        push("c <- b[a]");
        parse();
    }
    
    @Test(expected=InterpreterException.class)
    public void arrayIndexingANonArray() throws Exception {
        push("a <- 1");
        push("OUTPUT a[0]");
        parse();
    }
    
    @Test
    public void arrayElementAssignment() throws Exception {
        push("a <- [1, 2, 3, 4]");
        push("a[2] <- 'HELLO'");
        push("OUTPUT a[2]");
        parse();
        check("HELLO");
    }

    @Test
    public void arrayElementAssignmentFromArrayIndex() throws Exception {
        push("a <- [1, 2]");
        push("a[0] <- a[1]");
        push("OUTPUT a");
        parse();
        check("22");
    }
    
    @Test
    public void arrayLength() throws Exception {
        parseAndCheck("OUTPUT LEN([1, 2])", "2");
    }
    
    @Test
    public void inspectString() throws Exception {
        parseAndCheck("INSPECT 'h'", "'h'");
    }
    
    @Test
    public void ifWhenTrue() throws Exception {
        push("a <- (1 < 2)");
        push("IF a THEN");
        push("OUTPUT 200");
        push("ENDIF");
        parse();
        check("200");
    }

    @Test
    public void ifWhenFalse() throws Exception {
        push("a <- (1 < 0)");
        push("IF a THEN");
        push("OUTPUT 200");
        push("ENDIF");
        parse();
        check("");
    }

    @Test
    public void ifElseWhenTrue() throws Exception {
        push("a <- (1 < 2)");
        push("IF a THEN");
        push("OUTPUT 200");
        push("ELSE");
        push("OUTPUT 100");
        push("ENDIF");
        parse();
        check("200");
    }

    @Test
    public void ifElseWhenFalse() throws Exception {
        push("a <- (1 < 0)");
        push("IF a THEN");
        push("OUTPUT 200");
        push("ELSE");
        push("OUTPUT 100");
        push("ENDIF");
        parse();
        check("100");
    }

    @Test
    public void ifElseIfElse1() throws Exception {
        push("IF TRUE THEN");
        push("OUTPUT 1");
        push("ELSE IF FALSE THEN");
        push("OUTPUT 2");
        push("ELSE");
        push("OUTPUT 3");
        push("ENDIF");
        parse();
        check("1");
    }
    
    @Test
    public void ifElseIfElse2() throws Exception {
        push("IF FALSE THEN");
        push("OUTPUT 1");
        push("ELSE IF TRUE THEN");
        push("OUTPUT 2");
        push("ELSE");
        push("OUTPUT 3");
        push("ENDIF");
        parse();
        check("2");
    }

    @Test
    public void ifElseIfElse3() throws Exception {
        push("IF FALSE THEN");
        push("OUTPUT 1");
        push("ELSE IF FALSE THEN");
        push("OUTPUT 2");
        push("ELSE");
        push("OUTPUT 3");
        push("ENDIF");
        parse();
        check("3");
    }

    @Test
    public void whileLoop() throws Exception {
        push("a <- ''");
        push("i <- 0");
        push("WHILE i < 3");
        push("a <- a + INT_TO_STRING(i)");
        push("i <- i + 1");
        push("ENDWHILE");
        push("OUTPUT a");
        parse();
        check("012");
    }

    @Test
    public void repeatLoop() throws Exception {
        push("a <- ''");
        push("i <- 0");
        push("REPEAT");
        push("a <- a + INT_TO_STRING(i)");
        push("i <- i + 2");
        push("UNTIL i > 6");
        push("OUTPUT a");
        parse();
        check("0246");
    }

    @Test
    public void forLoop() throws Exception {
        push("a <- ''");
        push("FOR i <- 0 TO 3");
        push("a <- a + INT_TO_STRING(i)");
        push("ENDFOR");
        push("OUTPUT a");
        parse();
        check("0123");
    }

    @Test
    public void emptyForLoop() throws Exception {
        push("FOR i <- 0 TO 1");
        push("ENDFOR");
        push("OUTPUT i");
        parse();
        check("1");

        reset();
        push("FOR i <- 0 TO -1");
        push("ENDFOR");
        push("OUTPUT i");
        parse();
        check("0");
    }

    @Test
    public void nestedFor() throws Exception {
        push("FOR i <- 0 TO 1");
        push("FOR j <- 0 TO 2");
        push("ENDFOR");
        push("ENDFOR");
        push("OUTPUT i + j");
        parse();
        check("3");
    }

    @Test
    public void nestedIf() throws Exception {
        push("IF TRUE THEN");
        push("IF TRUE THEN");
        push("OUTPUT 23");
        push("ENDIF");
        push("ENDIF");
        parse();
        check("23");
    }

    @Test
    public void nestedWhile() throws Exception {
        push("a <- 0");
        push("b <- 0");
        push("WHILE (a < 10)");
        push("WHILE (b < 5)");
        push("b <- b + 1");
        push("ENDWHILE");
        push("a <- a + 1");
        push("ENDWHILE");
        push("OUTPUT a + b");
        parse();
        check("15");
    }

    @Test
    public void nestedRepeat() throws Exception {
        push("a <- 0");
        push("b <- 0");
        push("REPEAT");
        push("REPEAT");
        push("b <- b + 1");
        push("UNTIL b > 20");
        push("a <- a + 1");
        push("UNTIL a > 10");
        push("OUTPUT a + b");
        parse();
        check("42");
    }

    @Test
    public void userInput() throws Exception {
        pushInput("Hello");
        push("a <- USERINPUT");
        push("OUTPUT a");
        parse();
        check("Hello");
    }

    @Test
    public void constantVariableDeclaration() throws Exception {
        parseAndCheck("constant abacus <- 'Hello'\nOUTPUT abacus", "Hello");
    }

    @Test(expected = Exception.class)
    public void cannotChangeConstantVariable() throws Exception {
        push("constant var <- 12");
        push("var <- 2");
        parse();
    }

    @Test
    public void parenthesizedExpressions() throws Exception {
        parseAndCheck("OUTPUT (8+2)*3", "30");
        parseAndCheck("OUTPUT 8+(2*3)", "14");
    }
    
    @Test
    public void assigningToA2DArray() throws Exception {
        push("ary <- [[]]");
        push("ary[0][0] <- 1");
        push("OUTPUT ary[0]");
        parse();
        check("1");
    }

    @Test
    public void readingFrom2DArray() throws Exception {
        push("ary <- [[1, 2], [3, 4]]");
        push("OUTPUT ary[0][0]");
        parse();
        check("1");
    }
    
    @Test(expected=InterpreterException.class)
    public void lengthOnSomethingThatDoesNotHaveALength() throws Exception {
        parseAndCheck("LEN(23)", "NOTUSED");
    }

    private void push(String code) {
        codeToParse += code + "\n";
    }

    /**
     * Clear the code being parsed - so we can start pushing again.
     */
    private void reset() {
        codeToParse = "";
    }

    private void pushInput(String str) {
        inputProvider.inputData += str + "\n";
    }

    private void parse() throws Exception {
        subject = new Interpreter(new StringReader(codeToParse), outputObserver, inputProvider);
        subject.execute();
    }

    private void parse(String code) throws Exception {
        codeToParse = code;
        parse();
    }

    private void check(String expected) {
        assertEquals(expected, outputObserver.message);
    }

    private void parseAndCheck(String code, String expected) throws Exception {
        parse(code);
        check(expected);
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
            inputData = "";
        }

        @Override
        public String getInput() {
            return inputData;
        }

    }

}
