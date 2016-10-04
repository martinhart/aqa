/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

import aqa.InterpreterException;
import aqa.subroutine.Subroutine;
import aqa.tokenizer.Tokens;
import aqa.tokenizer.Token;
import aqa.tokenizer.TokenSequencer;
import aqa.value.ArrayValue;
import aqa.value.IntegerValue;
import aqa.value.StringValue;
import aqa.value.Value;
import aqa.value.ValueFactory;
import aqa.variable.Variable;
import aqa.variable.Variables;
import aqa.variable.VariablesImpl;
import java.util.Random;

/**
 *
 * @author martinhart
 */
public class Parser {

    private final OutputWriter outputWriter;
    private final InputProvider inputProvider;
    private final InstructionListener instructionListener;
    Tokens tokens;
    private TokenSequencer tokenSequencer;
    private VirtualMachine vm;

    public Parser(OutputWriter outputWriter, InputProvider inputProvider,
            InstructionListener instructionListener) {
        this.outputWriter = outputWriter;
        this.inputProvider = inputProvider;
        this.instructionListener = instructionListener;
        this.tokens = new Tokens();  
        vm = new VirtualMachine();
    }
    
    public Parser(OutputWriter outputWriter, InputProvider inputProvider) {
        this(outputWriter, inputProvider, new InstructionListener());
    }
    
    public Parser(InstructionListener instructionListener) {
        this(new NullOutputWriter(), new NullInputProvider(), instructionListener);
    }

    public Parser() {
        this(new NullOutputWriter(), new NullInputProvider());
    }
    
    public VirtualMachine getVM() {
        return vm;
    }

    /**
     * Iterate through the tokens, parsing and executing statements accordingly.
     *
     * @throws InterpreterException
     */
    public void parse() throws InterpreterException {
        tokenSequencer = new TokenSequencer(tokens);
        try {
            outerBlock();
        }
        catch(InterpreterException e) {
            throw new InterpreterException(tokenSequencer.getCurrentTokenLine(), e.getLocalizedMessage());
        }
    }

    /**
     * Add a token to be parsed.
     *
     * @param t - the Token to parse
     */
    public void addToken(Token t) {
        tokens.append(t);
    }

    private void outerBlock() throws InterpreterException {
        while (tokenSequencer.thereIsAToken()) {
            if (tokenSequencer.match("SUBROUTINE")) {
                subroutine();
            } else {
                block();
            }
        }
    }

    private void subroutine() throws InterpreterException {
        Subroutine s;

        tokenSequencer.expect("SUBROUTINE");
        s = new Subroutine(tokenSequencer.getCurrentTokenName());
        tokenSequencer.advance();
        tokenSequencer.expect("(");
        while (!tokenSequencer.match(")") && tokenSequencer.thereIsAToken()) {
            s.addParameter(tokenSequencer.getCurrentTokenName());
            tokenSequencer.advance();
            if (!tokenSequencer.match(")")) {
                tokenSequencer.expect(",");
            }
        }
        tokenSequencer.expect(")");
        while (!tokenSequencer.match("ENDSUBROUTINE") && tokenSequencer.thereIsAToken()) {
            s.addToken(tokenSequencer.getCurrentToken());
            tokenSequencer.advance();
        }
        tokenSequencer.expect("ENDSUBROUTINE");
        vm.addSubroutine(s);
    }
    
    private void subroutineCall() throws InterpreterException {
        Subroutine s = vm.getSubroutine(tokenSequencer.getCurrentTokenName());
        VirtualMachine currentVM = vm;
        TokenSequencer currentTokenSequencer = tokenSequencer;
        Variables subroutineVariables = new VariablesImpl();
        int paramIndex = 0;
        
        tokenSequencer.advance();
        tokenSequencer.expect("(");
        while (!tokenSequencer.match(")") && tokenSequencer.thereIsAToken()) {
            Variable argument = new Variable(s.getParameter(paramIndex));
            booleanExpression();
            argument.setValue(vm.popValue().makeCopy());
            subroutineVariables.set(argument);
            ++paramIndex;
            if (!tokenSequencer.match(")")) {
                tokenSequencer.expect(",");
            }
        }
        tokenSequencer.expect(")");
        
        this.vm = new VirtualMachine(vm.getSubroutines(), subroutineVariables);
        this.tokenSequencer = new TokenSequencer(s.getTokens());
        block();
        try {
            currentVM.pushValue(vm.popValue());
        } catch(InterpreterException e) {
            // ignore - there's no return value.
        }
        this.vm = currentVM;
        this.tokenSequencer = currentTokenSequencer;
    }

    private void block() throws InterpreterException {
        while (tokenSequencer.thereIsAToken()) {
            instruction();
        }
    }

    private void instruction() throws InterpreterException {
        instructionListener.newInstruction(tokenSequencer.getCurrentTokenLine());
        
        if (tokenSequencer.match("constant")) {
            assignment();
        } else {
            tokenSequencer.advance(); // skip variable name
            if (tokenSequencer.match("<")) { // could be an assignment to variable a <- 1
                tokenSequencer.advance();
                if (tokenSequencer.match("-")) {
                    tokenSequencer.retreat();
                    tokenSequencer.retreat();
                    assignment();
                } else {
                    tokenSequencer.retreat();
                    tokenSequencer.retreat();
                    statement();
                }
            } else // could be array assignment a[2] <- 3
            {
                if (tokenSequencer.match("[")) {
                    tokenSequencer.retreat();
                    assignment();
                } else {
                    tokenSequencer.retreat();
                    statement();
                }
            }
        }
    }

    private void assignment() throws InterpreterException {
        try {
            Variable var = constantLValue();
            if (tokenSequencer.match("[")) { // 'ary[0] <-' or 'ary[0][1] <-'
                tokenSequencer.expect("[");
                booleanExpression();
                int arrayIndex = vm.getValueStack().popInteger();
                tokenSequencer.expect("]");
                if (tokenSequencer.match("[")) { // ary[0][1] <- 
                    tokenSequencer.expect("[");
                    booleanExpression();
                    int childIndex = vm.getValueStack().popInteger();
                    tokenSequencer.expect("]");
                    tokenSequencer.expect("<");
                    tokenSequencer.expect("-");
                    statement();
                    ArrayValue parentArray = (ArrayValue) var.getValue();
                    ArrayValue childArray = (ArrayValue) parentArray.getAtIndex(arrayIndex);
                    childArray.setAtIndex(childIndex, vm.popValue());
                } else {
                    tokenSequencer.expect("<");
                    tokenSequencer.expect("-");
                    statement();
                    ((ArrayValue) var.getValue()).setAtIndex(arrayIndex, vm.popValue());
                }
            } else {
                tokenSequencer.expect("<");
                tokenSequencer.expect("-");
                statement();
                var.setValue(vm.popValue());
            }
        } catch (ClassCastException e) {
            throw new InterpreterException("invalid array assignment");
        }
    }

    private Variable constantLValue() throws InterpreterException {
        Variable var;

        if (tokenSequencer.match("constant")) {
            tokenSequencer.expect("constant");
            var = lValue();
            var.makeConstant();
        } else {
            var = lValue();
        }

        return var;
    }

    private Variable lValue() {
        Variable v = vm.getVariable(tokenSequencer.getCurrentTokenName());
        tokenSequencer.advance();
        return v;
    }

    private void statement() throws InterpreterException {
        if (tokenSequencer.match("OUTPUT")) {
            output();
        } else if (tokenSequencer.match("INSPECT")) {
            inspect();
        } else if (tokenSequencer.match("IF")) {
            ifStatement();
        } else if (tokenSequencer.match("REPEAT")) {
            repeatStatement();
        } else if (tokenSequencer.match("WHILE")) {
            whileStatement();
        } else if (tokenSequencer.match("FOR")) {
            forStatement();
        } else {
            booleanExpression();
        }
    }

    private void output() throws InterpreterException {
        tokenSequencer.expect("OUTPUT");
        statement();
        outputWriter.output(vm.popValue().output());
    }

    private void inspect() throws InterpreterException {
        tokenSequencer.expect("INSPECT");
        statement();
        outputWriter.output(vm.popValue().inspect());
    }

    private void ifStatement() throws InterpreterException {
        if (ifStatementCondition()) {
            ifStatementBlock();
        } else {
            while (!tokenSequencer.match("ENDIF") && tokenSequencer.thereIsAToken()) {
                if (tokenSequencer.match("ELSE")) {
                    tokenSequencer.expect("ELSE");
                    if (tokenSequencer.match("IF")) {
                        elseIfStatement();
                    } else {
                        ifStatementBlock();
                    }
                } else {
                    tokenSequencer.advance();
                }
            }
        }
        tokenSequencer.expect("ENDIF");
    }

    private void elseIfStatement() throws InterpreterException {
        if (ifStatementCondition()) {
            ifStatementBlock();
        }
    }

    /**
     * Chomp IF <cond> THEN
     *
     * @return <cond> == true
     * @throws InterpreterException
     */
    private boolean ifStatementCondition() throws InterpreterException {
        tokenSequencer.expect("IF");
        booleanExpression();
        tokenSequencer.expect("THEN");
        return (vm.getValueStack().popBoolean());
    }

    /**
     * Execute commands in current block, skip commands after ELSE. Finish at
     * ENDIF.
     *
     * @throws InterpreterException
     */
    private void ifStatementBlock() throws InterpreterException {
        boolean haveSeenElse = false;
        while (!tokenSequencer.match("ENDIF") && tokenSequencer.thereIsAToken()) {
            if (tokenSequencer.match("ELSE")) {
                haveSeenElse = true;
            }
            if (!haveSeenElse) {
                instruction();
            } else {
                tokenSequencer.advance();
            }
        }
    }

    private void whileStatement() throws InterpreterException {
        int indexOfCondition, indexOfEndWhile = 0;

        tokenSequencer.expect("WHILE");
        indexOfCondition = tokenSequencer.getCurrentIndex();
        booleanExpression();
        if (vm.getValueStack().popBoolean()) {
            do {
                while (!tokenSequencer.match("ENDWHILE") && tokenSequencer.thereIsAToken()) {
                    instruction();
                }
                indexOfEndWhile = tokenSequencer.getCurrentIndex();
                tokenSequencer.jumpToPosition(indexOfCondition);
                booleanExpression();
            } while (vm.getValueStack().popBoolean());
        } else {
            while (!tokenSequencer.match("ENDWHILE") && tokenSequencer.thereIsAToken()) {
                tokenSequencer.advance();
            }
            indexOfEndWhile = tokenSequencer.getCurrentIndex();
        }
        tokenSequencer.jumpToPosition(indexOfEndWhile);
        tokenSequencer.expect("ENDWHILE");
    }

    private void repeatStatement() throws InterpreterException {
        int nextStatement = -1, firstStatement = -1;

        tokenSequencer.expect("REPEAT");
        firstStatement = tokenSequencer.getCurrentIndex();
        do {
            tokenSequencer.jumpToPosition(firstStatement);
            while (!tokenSequencer.match("UNTIL") && tokenSequencer.thereIsAToken()) {
                instruction();
            }
            tokenSequencer.expect("UNTIL");
            booleanExpression();
            nextStatement = tokenSequencer.getCurrentIndex();
        } while (!vm.getValueStack().popBoolean());
        tokenSequencer.jumpToPosition(nextStatement);
    }

    /**
     * FOR i <- 0 TO 3 statements ENDFOR @throws InterpreterException
     *
     */
    private void forStatement() throws InterpreterException {
        int i, endValue, startIndex, endIndex = -1;
        String variableName;
        Variable loopCounter;
    
        tokenSequencer.expect("FOR");
        variableName = tokenSequencer.getCurrentTokenName();
        assignment();
        loopCounter = vm.getVariable(variableName);

        tokenSequencer.expect("TO");
        booleanExpression();
        endValue = vm.getValueStack().popInteger();
        startIndex = tokenSequencer.getCurrentIndex();

        if ((int) loopCounter.getValue().getValue() <= endValue) {
            while ((int) loopCounter.getValue().getValue() <= endValue) { // actually loop
                while (!tokenSequencer.match("ENDFOR") && tokenSequencer.thereIsAToken()) {
                    instruction();
                }
                endIndex = tokenSequencer.getCurrentIndex();
                tokenSequencer.jumpToPosition(startIndex);
                loopCounter.setValue(loopCounter.getValue().add(new IntegerValue(1)));
            }
            loopCounter.setValue(new IntegerValue(endValue));
        }

        if (endIndex > -1) {
            tokenSequencer.jumpToPosition(endIndex);
        }
        tokenSequencer.expect("ENDFOR");
    }

    private void length() throws InterpreterException {
        Value rvalue;
        tokenSequencer.expect("LEN");
        tokenSequencer.expect("(");
        statement();
        rvalue = vm.peekValue();
        if (rvalue instanceof StringValue) {
            vm.getValueStack().push(vm.getValueStack().popString().length());
        }
        else if (rvalue instanceof ArrayValue) {
            ArrayValue av = (ArrayValue) vm.popValue();
            vm.pushValue(av.length());
        }
        else {
            throw new InterpreterException("cannot call LEN on " + rvalue.inspect());
        }
        tokenSequencer.expect(")");
    }

    private void stringPosition() throws InterpreterException {
        String lvalue, rvalue;
        tokenSequencer.expect("POSITION");
        tokenSequencer.expect("(");
        statement();
        lvalue = vm.getValueStack().popString();
        tokenSequencer.expect(",");
        statement();
        rvalue = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        vm.getValueStack().push(lvalue.indexOf(rvalue));
    }

    private void stringSubstring() throws InterpreterException {
        int start, end;
        String str;
        tokenSequencer.expect("SUBSTRING");
        tokenSequencer.expect("(");
        statement();
        start = vm.getValueStack().popInteger();
        tokenSequencer.expect(",");
        statement();
        end = vm.getValueStack().popInteger();
        tokenSequencer.expect(",");
        statement();
        str = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        vm.getValueStack().push(str.substring(start, end + 1));
    }

    private void stringToInt() throws InterpreterException {
        String str;
        tokenSequencer.expect("STRING_TO_INT");
        tokenSequencer.expect("(");
        statement();
        str = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        try {
            vm.getValueStack().push(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            throw new InterpreterException("invalid integer format: '" + str + "'");
        }
    }

    private void stringToReal() throws InterpreterException {
        String str;
        tokenSequencer.expect("STRING_TO_REAL");
        tokenSequencer.expect("(");
        statement();
        str = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        try {
            vm.getValueStack().push(Double.parseDouble(str));
        } catch (NumberFormatException e) {
            throw new InterpreterException("invalid real format '" + str + "'");
        }
    }

    private void intToString() throws InterpreterException {
        int i;
        tokenSequencer.expect("INT_TO_STRING");
        tokenSequencer.expect("(");
        statement();
        i = vm.getValueStack().popInteger();
        tokenSequencer.expect(")");
        vm.getValueStack().push(Integer.toString(i));
    }

    private void realToString() throws InterpreterException {
        double d;
        tokenSequencer.expect("REAL_TO_STRING");
        tokenSequencer.expect("(");
        statement();
        d = vm.getValueStack().popReal();
        tokenSequencer.expect(")");
        vm.getValueStack().push(Double.toString(d));
    }

    private void charToCode() throws InterpreterException {
        String s;
        tokenSequencer.expect("CHAR_TO_CODE");
        tokenSequencer.expect("(");
        statement();
        s = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        vm.getValueStack().push((int) s.charAt(0));
    }

    private void codeToChar() throws InterpreterException {
        int i;
        tokenSequencer.expect("CODE_TO_CHAR");
        tokenSequencer.expect("(");
        statement();
        i = vm.getValueStack().popInteger();
        tokenSequencer.expect(")");
        vm.getValueStack().push(String.valueOf((char) i));
    }

    private void userInput() throws InterpreterException {
        String input;
        tokenSequencer.expect("USERINPUT");
        input = inputProvider.getInput();
        vm.getValueStack().push(input.trim());
    }

    private void randomInt() throws InterpreterException {
        int start, end;
        tokenSequencer.expect("RANDOM_INT");
        tokenSequencer.expect("(");
        statement();
        start = vm.getValueStack().popInteger();
        tokenSequencer.expect(",");
        statement();
        end = vm.getValueStack().popInteger();
        tokenSequencer.expect(")");
        vm.getValueStack().push(new Random().nextInt(end - start + 1) + start);
    }

    private void booleanExpression() throws InterpreterException {
        booleanTerm();
        while (tokenSequencer.match("OR") && tokenSequencer.thereIsAToken()) {
            boolean lvalue, rvalue;
            lvalue = vm.getValueStack().popBoolean();
            tokenSequencer.expect("OR");
            booleanTerm(); //TODO - should this be booleanExpression?
            rvalue = vm.getValueStack().popBoolean();
            if (lvalue || rvalue) {
                vm.getValueStack().push(true);
            }
            else {
                vm.getValueStack().push(false);
            }
        }
    }

    private void booleanTerm() throws InterpreterException {
        notFactor();
        while (tokenSequencer.match("AND") && tokenSequencer.thereIsAToken()) {
            boolean lvalue, rvalue;
            lvalue = vm.getValueStack().popBoolean();
            tokenSequencer.expect("AND");
            notFactor(); // TODO - should this be booleanExpression?
            rvalue = vm.getValueStack().popBoolean();
            if (lvalue && rvalue) {
                vm.getValueStack().push(true);
            }
            else {
                vm.getValueStack().push(false);
            }
        }
    }

    private void notFactor() throws InterpreterException {
        if (tokenSequencer.match("NOT")) {
            tokenSequencer.expect("NOT");
            relation();
            vm.getValueStack().push(!vm.getValueStack().popBoolean());
        } else {
            relation();
        }
    }

    private void relation() throws InterpreterException {
        expression();
        while (tokenSequencer.match("<") || tokenSequencer.match(">") || tokenSequencer.match("=") || tokenSequencer.match("!")) {
            Value lvalue = vm.popValue(), rvalue;
            if (tokenSequencer.match("<")) {
                tokenSequencer.advance();
                if (tokenSequencer.match("=")) {
                    tokenSequencer.advance();
                    booleanExpression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) < 1);
                } else {
                    booleanExpression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) < 0);
                }
            } else if (tokenSequencer.match(">")) {
                tokenSequencer.advance();
                if (tokenSequencer.match("=")) {
                    tokenSequencer.advance();
                    booleanExpression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) > -1);
                } else {
                    booleanExpression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) > 0);
                }
            } else if (tokenSequencer.match("=")) {
                tokenSequencer.advance();
                booleanExpression();
                rvalue = vm.popValue();
                vm.getValueStack().push(lvalue.equal(rvalue));
            } else if (tokenSequencer.match("!")) {
                tokenSequencer.advance();
                tokenSequencer.expect("=");
                booleanExpression();
                rvalue = vm.popValue();
                vm.getValueStack().push(!lvalue.equal(rvalue));
            }
        }
    }

    private void expression() throws InterpreterException {
        term();
        while (tokenSequencer.match("+") || tokenSequencer.match("-")) {
            Value lvalue = vm.popValue(), rvalue;
            if (tokenSequencer.match("+")) {
                tokenSequencer.expect("+");
                term();
                rvalue = vm.popValue();
                vm.pushValue(lvalue.add(rvalue));
            } else if (tokenSequencer.match("-")) {
                tokenSequencer.expect("-");
                term();
                rvalue = vm.popValue();
                vm.pushValue(lvalue.subtract(rvalue));
            }
        }
    }

    private void term() throws InterpreterException {
        factor();
        if ((tokenSequencer.match("*") || (tokenSequencer.match("/") || tokenSequencer.match("DIV") || tokenSequencer.match("MOD")))) {
            Value lvalue = vm.popValue(), rvalue;
            if (tokenSequencer.match("*")) {
                tokenSequencer.expect("*");
                factor();
                rvalue = vm.popValue();
                vm.pushValue(lvalue.multiply(rvalue));
            } else if (tokenSequencer.match("/")) {
                tokenSequencer.expect("/");
                factor();
                rvalue = vm.popValue();
                vm.pushValue(lvalue.divide(rvalue));
            } else if (tokenSequencer.match("DIV")) {
                tokenSequencer.expect("DIV");
                factor();
                rvalue = vm.popValue();
                vm.pushValue(lvalue.div(rvalue));
            } else if (tokenSequencer.match("MOD")) {
                tokenSequencer.expect("MOD");
                factor();
                rvalue = vm.popValue();
                vm.pushValue(lvalue.mod(rvalue));
            }
        }
    }

    private void factor() throws InterpreterException {
        if (tokenSequencer.match("(")) {
            tokenSequencer.expect("(");
            booleanExpression();
            tokenSequencer.expect(")");
        } else {
            literal();
        }
    }

    private void literal() throws InterpreterException {
        //TODO
        if (isStringLiteral()) {
            stringLiteral();
        } else if (tokenSequencer.match("TRUE") || tokenSequencer.match("FALSE")) {
            booleanLiteral();
        } else if (isNumberLiteral()) {
            numberLiteral();
        } else if (tokenSequencer.match("[")) {
            arrayLiteral();
        } else if (tokenSequencer.match("LEN")) {
            length();
        } else if (tokenSequencer.match("POSITION")) {
            stringPosition();
        } else if (tokenSequencer.match("SUBSTRING")) {
            stringSubstring();
        } else if (tokenSequencer.match("STRING_TO_INT")) {
            stringToInt();
        } else if (tokenSequencer.match("STRING_TO_REAL")) {
            stringToReal();
        } else if (tokenSequencer.match("INT_TO_STRING")) {
            intToString();
        } else if (tokenSequencer.match("REAL_TO_STRING")) {
            realToString();
        } else if (tokenSequencer.match("CHAR_TO_CODE")) {
            charToCode();
        } else if (tokenSequencer.match("CODE_TO_CHAR")) {
            codeToChar();
        } else if (tokenSequencer.match("USERINPUT")) {
            userInput();
        } else if (tokenSequencer.match("RANDOM_INT")) {
            randomInt();
        } else {
            tokenSequencer.advance();
            if (tokenSequencer.match("(")) {
                tokenSequencer.retreat();
                subroutineCall();
            }
            else {
                tokenSequencer.retreat();
                variable();
            }
        }
    }

    private boolean isStringLiteral() {
        return (tokenSequencer.getCurrentTokenName().charAt(0) == '\'');
    }

    private boolean isNumberLiteral() {
        String tok = tokenSequencer.getCurrentTokenName();
        return (Character.isDigit(tok.charAt(0)) || tok.charAt(0) == '-');
    }

    private void variable() throws InterpreterException {
        Variable v = vm.getVariable(tokenSequencer.getCurrentTokenName());
        tokenSequencer.advance();
        if (tokenSequencer.match("[")) { // array indexing
            try {
                ArrayValue ary = (ArrayValue) v.getValue();
                int index;
                tokenSequencer.expect("[");
                statement();
                index = vm.getValueStack().popInteger();
                tokenSequencer.expect("]");
                if (tokenSequencer.match("[")) { // 2d array
                    ary = (ArrayValue) ary.getAtIndex(index);
                    tokenSequencer.expect("[");
                    statement();
                    index = vm.getValueStack().popInteger();
                    tokenSequencer.expect("]");
                    vm.pushValue(ary.getAtIndex(index));
                } else {
                    vm.pushValue(ary.getAtIndex(index));
                }
            }
            catch(ClassCastException e) {
                throw new InterpreterException("attempt to index variable '" + v.getName() + "' as if it were an array");
            }
        }
        else {
            vm.pushValue(v.getValue());
        }
    }

    private void stringLiteral() throws InterpreterException {
        vm.pushValue(ValueFactory.createString(tokenSequencer.getCurrentToken()));
        tokenSequencer.advance();
    }

    private void booleanLiteral() throws InterpreterException {
        vm.pushValue(ValueFactory.createBoolean(tokenSequencer.getCurrentToken()));
        tokenSequencer.advance();
    }

    private void arrayLiteral() throws InterpreterException {
        ArrayValue ary = new ArrayValue();
        int index = 0;
        
        tokenSequencer.expect("[");
        if (!tokenSequencer.match("]")) {
            statement();
            ary.setAtIndex(index, vm.popValue());
            ++index;

            while (tokenSequencer.match(",")) {
                tokenSequencer.advance();
                statement();
                ary.setAtIndex(index, vm.popValue());
                ++index;
            }
        }
        tokenSequencer.expect("]");
        vm.pushValue(ary);
    }

    private void numberLiteral() throws InterpreterException {
        // This could be a negative number literal - in which case we need
        // to combine the tokens '-' and the number.
        if (tokenSequencer.match("-")) {
            tokenSequencer.advance();
            tokenSequencer.getCurrentToken().setName("-" + tokenSequencer.getCurrentTokenName());
        }
        if (contains(".")) {
            realLiteral();
        } else {
            integerLiteral();
        }
    }

    private boolean contains(String value) {
        return tokenSequencer.contains(value);
    }

    private void realLiteral() throws InterpreterException {
        vm.pushValue(ValueFactory.createReal(tokenSequencer.getCurrentToken()));
        tokenSequencer.advance();
    }

    private void integerLiteral() throws InterpreterException {
        vm.pushValue(ValueFactory.createInteger(tokenSequencer.getCurrentToken()));
        tokenSequencer.advance();
    }
}
