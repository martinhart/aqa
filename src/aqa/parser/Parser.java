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
import aqa.variable.VariableTable;
import aqa.variable.VariableTableImpl;
import java.util.Random;

/**
 * This class contains the implementation of a recursive descent parser for
 * the AQA Pseudocode Language.
 * 
 * In order to run the parser you need to:
 * 1. Construct an instance with the necessary callback objects.
 * 2. Add some tokens to parse.
 * 3. Call parse.
 * 
 * TODO: refactoring - this class is too big.  Can we split into smaller parsers,
 * each responsible for a BNF statement?
 * 
 * TODO: refactoring - why can't we just pass the tokens into the constructor?
 * why are we adding separate tokens one at a time?
 * 
 * @author martinhart
 */
public class Parser {
    
    /**
     * The object that will provide the user with output (OUTPUT and INSPECT)
     */
    private final OutputWriter outputWriter;
    
    /**
     * The object that will provide the parser with user input during
     * execution (USERINPUT)
     */
    private final InputProvider inputProvider;
    
    /**
     * The object to notify when we move on to a new instruction.
     */
    private final InstructionListener instructionListener;
    
    /**
     * The tokens to parse and execute
     */
    Tokens tokens;
    
    /**
     * A wrapper object to keep track of which token we're working on.
     */
    private TokenSequencer tokenSequencer;
    
    /**
     * The current virtual machine (variable table, value stack, subroutine table)
     */
    private VirtualMachine vm;
    
    /**
     * Create a new instance of the parser ready to go
     * @param outputWriter where program output should go
     * @param inputProvider where user input should come from
     * @param instructionListener who to notify about current state
     */
    public Parser(OutputWriter outputWriter, InputProvider inputProvider,
            InstructionListener instructionListener) {
        this.outputWriter = outputWriter;
        this.inputProvider = inputProvider;
        this.instructionListener = instructionListener;
        this.tokens = new Tokens();  
        vm = new VirtualMachine();
    }
    
    /**
     * Create a new instance of the parser without notifying clients regarding
     * current state
     * @param outputWriter where program output should go
     * @param inputProvider where user input should come from
     */
    public Parser(OutputWriter outputWriter, InputProvider inputProvider) {
        this(outputWriter, inputProvider, new IgnoreInstructionListener());
    }
    
    /**
     * Create a new instance of the parser that does not have the capability
     * of providing output to the user or reading input from the user.  It is
     * able to notify about current state.
     * 
     * It is unwise to construct this way in production unless your program does
     * not need any user interaction.  It is useful for testing.
     * 
     * @param instructionListener  who to notify about current state
     */
    public Parser(InstructionListener instructionListener) {
        this(new NullOutputWriter(), new NullInputProvider(), instructionListener);
    }

    /**
     * Create a new instance of the parser that does not interact in any way
     * with the outside world.
     * 
     * It is unwise to use this constructor unless you are testing the parser.
     */
    public Parser() {
        this(new NullOutputWriter(), new NullInputProvider());
    }
    
    /**
     * Get the current virtual machine state of this parser.
     * @return the vm
     */
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

    /**
     * The outer block is the main program.  It is inside an outer block that
     * SUBROUTINE can be defined so we catch them here.
     * 
     * @throws InterpreterException
     */
    private void outerBlock() throws InterpreterException {
        while (tokenSequencer.thereIsAToken()) {
            if (tokenSequencer.match("SUBROUTINE")) {
                subroutine();
            } else {
                block();
            }
        }
    }

    /**
     * Handle a subroutine definition.
     * 
     * @throws InterpreterException 
     */
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
    
    /**
     * Execute a subroutine call.  To achieve this we need to take the following
     * steps.
     * 
     * 1. store current context
     * 2. create a context for the subroutine.
     * 3. get values of arguments from the call line, and create variables
     *      to place in the new context.
     * 4. parse the subroutine tokens in the new context.
     * 5. get a return value (if any) and push onto old context
     * 6. replace context.
     * 
     * @throws InterpreterException if execution fails
     * @throws ReturnException if early return from anywhere (subfunction) is triggered.
     * 
     * TODO: this function does too much.  break it down.
     */
    private void subroutineCall() throws InterpreterException, ReturnException  {
        Subroutine s = vm.getSubroutine(tokenSequencer.getCurrentTokenName());
        VirtualMachine currentVM = vm;
        TokenSequencer currentTokenSequencer = tokenSequencer;
        VariableTable subroutineVariables = new VariableTableImpl();
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
        
        this.vm = new VirtualMachine(vm.getSubroutineTable(), subroutineVariables);
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

    /**
     * A Block is a set of instructions.
     * 
     * @throws InterpreterException if an error occurs
     */
    private void block() throws InterpreterException {
        try {
            while (tokenSequencer.thereIsAToken()) {
                instruction();
            }
        } catch(ReturnException e) {
            // ignore
        }
    }

    /**
     * An instruction is either an assignment or a statement.
     * 
     * @throws InterpreterException if an error occurs
     * @throws ReturnException if we are breaking out of a subroutine
     */
    private void instruction() throws InterpreterException, ReturnException {
        instructionListener.newInstruction(tokenSequencer.getCurrentTokenLine(), vm);
        
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

    /**
     * An assignment consists of constantLValue <- expression
     * 
     * TODO: this function is currently also handling array assignment, move out
     * to make intent clearer.
     * 
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void assignment() throws InterpreterException, ReturnException {
        try {
            Variable var = constantLValue();
            if (tokenSequencer.match("[")) { // 'ary[0] <-' or 'ary[0][1] <-'
                tokenSequencer.expect("[");
                expression();
                int arrayIndex = vm.getValueStack().popInteger();
                tokenSequencer.expect("]");
                if (tokenSequencer.match("[")) { // ary[0][1] <- 
                    tokenSequencer.expect("[");
                    expression();
                    int childIndex = vm.getValueStack().popInteger();
                    tokenSequencer.expect("]");
                    tokenSequencer.expect("<");
                    tokenSequencer.expect("-");
                    booleanExpression();
                    ArrayValue parentArray = (ArrayValue) var.getValue();
                    ArrayValue childArray = (ArrayValue) parentArray.getAtIndex(arrayIndex);
                    childArray.setAtIndex(childIndex, vm.popValue());
                } else {
                    tokenSequencer.expect("<");
                    tokenSequencer.expect("-");
                    booleanExpression();
                    ((ArrayValue) var.getValue()).setAtIndex(arrayIndex, vm.popValue());
                }
            } else {
                tokenSequencer.expect("<");
                tokenSequencer.expect("-");
                booleanExpression();
                var.setValue(vm.popValue());
            }
        } catch (ClassCastException e) {
            throw new InterpreterException("invalid array assignment");
        }
    }

    /**
     * Handle [constant] lvalues
     * @return the associated variable.
     * @throws InterpreterException 
     */
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

    /**
     * Handle an lvalue
     * @return the associated variable
     */
    private Variable lValue() {
        Variable v = vm.getVariable(tokenSequencer.getCurrentTokenName());
        tokenSequencer.advance();
        return v;
    }

    /**
     * A statement is either a built in control statement or a boolean expression
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void statement() throws InterpreterException, ReturnException {
        if (tokenSequencer.match("RETURN")) {
            tokenSequencer.advance();
            booleanExpression();
            throw new ReturnException();
        }
        else if (tokenSequencer.match("OUTPUT")) {
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

    /**
     * Process user output instruction
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void output() throws InterpreterException, ReturnException {
        tokenSequencer.expect("OUTPUT");
        booleanExpression();
        outputWriter.output(vm.popValue().output());
    }

    /**
     * Process debug output instruction
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void inspect() throws InterpreterException, ReturnException  {
        tokenSequencer.expect("INSPECT");
        booleanExpression();
        outputWriter.output(vm.popValue().inspect());
    }

    /**
     * Handle the IF statement
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void ifStatement() throws InterpreterException, ReturnException  {
        if (ifStatementCondition()) {
            ifStatementBlock();
        } else {
            // the original condition was false, so we need to see if there's
            // an else block to execute.
            while (!tokenSequencer.match("ENDIF") && tokenSequencer.thereIsAToken()) {
                if (tokenSequencer.match("ELSE")) {
                    tokenSequencer.expect("ELSE");
                    if (tokenSequencer.match("IF")) {
                        // there's an else if we need to handle.
                        elseIfStatement();
                    } else {
                        // simply execute the else block.
                        ifStatementBlock();
                    }
                } else {
                    tokenSequencer.advance();
                }
            }
        }
        tokenSequencer.expect("ENDIF");
    }

    /**
     * Deal with ELSE IF condition
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void elseIfStatement() throws InterpreterException, ReturnException  {
        if (ifStatementCondition()) {
            ifStatementBlock();
        }
    }

    /**
     * Handle the condition of an if statement:
     * 
     *  IF <cond> THEN
     *
     * @return <cond> == true
     * @throws InterpreterException
     */
    private boolean ifStatementCondition() throws InterpreterException, ReturnException  {
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
    private void ifStatementBlock() throws InterpreterException, ReturnException  {
        boolean haveSeenElse = false;
        while (!tokenSequencer.match("ENDIF") && tokenSequencer.thereIsAToken()) {
            if (tokenSequencer.match("ELSE")) {
                haveSeenElse = true;
            }
            if (!haveSeenElse) {
                // We are inside the body and we have not come to end of block
                // so we execute the line.
                instruction();
            } else {
                // We are inside the ELSE section, we shouldn't be executing
                // these statements since the original IF condition was true.
                tokenSequencer.advance();
            }
        }
    }

    /**
     * Handle WHILE statement constructs
     * @throws InterpreterException
     * @throws ReturnException 
     * 
     * TODO: break this down
     */
    private void whileStatement() throws InterpreterException, ReturnException  {
        int indexOfCondition, indexOfEndWhile = 0;

        tokenSequencer.expect("WHILE");
        indexOfCondition = tokenSequencer.getCurrentIndex();
        booleanExpression();
        if (vm.getValueStack().popBoolean()) {
            do {
                // The loop condition is true, so we execute all statements until
                // we come to end of loop.
                while (!tokenSequencer.match("ENDWHILE") && tokenSequencer.thereIsAToken()) {
                    instruction();
                }
                
                // we are at the end of the loop so we need to test the condition
                // again.
                indexOfEndWhile = tokenSequencer.getCurrentIndex();
                tokenSequencer.jumpToPosition(indexOfCondition);
                booleanExpression();
            } while (vm.getValueStack().popBoolean());
        } else {
            // the loop condition is false, so we skip all statements until we
            // come to the end of the loop.
            while (!tokenSequencer.match("ENDWHILE") && tokenSequencer.thereIsAToken()) {
                tokenSequencer.advance();
            }
            indexOfEndWhile = tokenSequencer.getCurrentIndex();
        }
        tokenSequencer.jumpToPosition(indexOfEndWhile);
        tokenSequencer.expect("ENDWHILE");
    }

    /**
     * Handle REPEAT statement constructs
     * 
     * TODO: break this down.
     * 
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void repeatStatement() throws InterpreterException, ReturnException  {
        int nextStatement = -1, firstStatement = -1;

        tokenSequencer.expect("REPEAT");
        firstStatement = tokenSequencer.getCurrentIndex();
        do {
            // execute the loop body.
            tokenSequencer.jumpToPosition(firstStatement);
            while (!tokenSequencer.match("UNTIL") && tokenSequencer.thereIsAToken()) {
                instruction();
            }
            tokenSequencer.expect("UNTIL");
            
            // check the loop condition.
            booleanExpression();
            nextStatement = tokenSequencer.getCurrentIndex();
        } while (!vm.getValueStack().popBoolean());
        
        tokenSequencer.jumpToPosition(nextStatement);
    }

    /**
     * Handle FOR statement constructs:
     * 
     *  FOR i <- 0 TO 3 statements ENDFOR 
     * 
     * TODO: break this down
     * 
     * @throws InterpreterException
     */
    private void forStatement() throws InterpreterException, ReturnException  {
        int i, endValue, startIndex, endIndex = -1;
        String variableName;
        Variable loopCounter;
    
        // find the variable containing loop counter and assign initial value
        tokenSequencer.expect("FOR");
        variableName = tokenSequencer.getCurrentTokenName();
        assignment();
        loopCounter = vm.getVariable(variableName);

        // find the end value and store it for later checking.
        tokenSequencer.expect("TO");
        expression();
        endValue = vm.getValueStack().popInteger();
        startIndex = tokenSequencer.getCurrentIndex();
        
        if ((int) loopCounter.getValue().getValue() <= endValue) {
            // The loop needs to run at least once.
            while ((int) loopCounter.getValue().getValue() <= endValue) { // actually loop
                while (!tokenSequencer.match("ENDFOR") && tokenSequencer.thereIsAToken()) {
                    instruction();
                }
                
                // increment loop counter
                endIndex = tokenSequencer.getCurrentIndex();
                tokenSequencer.jumpToPosition(startIndex);
                loopCounter.setValue(loopCounter.getValue().add(new IntegerValue(1)));
            }
            loopCounter.setValue(new IntegerValue(endValue));
        }
        else {
            // The initial loop condition means that the loop was never executed.
            // Ignore all tokens until we hit ENDFOR.
            while (!tokenSequencer.match("ENDFOR") && tokenSequencer.thereIsAToken()) {
                tokenSequencer.advance();
            }
            endIndex = tokenSequencer.getCurrentIndex();
        }

        if (endIndex > -1) {
            tokenSequencer.jumpToPosition(endIndex);
        }
        tokenSequencer.expect("ENDFOR");
    }

    /**
     * Handle LEN(...)
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void length() throws InterpreterException, ReturnException  {
        Value rvalue;
        tokenSequencer.expect("LEN");
        tokenSequencer.expect("(");
        expression();
        rvalue = vm.popValue();
        vm.pushValue(rvalue.length());
        tokenSequencer.expect(")");
    }

    /**
     * Handle POSITION(str, int)
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void stringPosition() throws InterpreterException, ReturnException  {
        String lvalue, rvalue;
        tokenSequencer.expect("POSITION");
        tokenSequencer.expect("(");
        expression();
        lvalue = vm.getValueStack().popString();
        tokenSequencer.expect(",");
        expression();
        rvalue = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        vm.getValueStack().push(lvalue.indexOf(rvalue));
    }

    /**
     * HANDLE SUBSTRING(start, end, string)
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void stringSubstring() throws InterpreterException, ReturnException  {
        int start, end;
        String str;
        tokenSequencer.expect("SUBSTRING");
        tokenSequencer.expect("(");
        expression();
        start = vm.getValueStack().popInteger();
        tokenSequencer.expect(",");
        expression();
        end = vm.getValueStack().popInteger();
        tokenSequencer.expect(",");
        expression();
        str = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        vm.getValueStack().push(str.substring(start, end + 1)); // +1 inclusive substring (see AQA spec)
    }

    /**
     * Handle STRING_TO_INT(str)
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void stringToInt() throws InterpreterException, ReturnException  {
        String str;
        tokenSequencer.expect("STRING_TO_INT");
        tokenSequencer.expect("(");
        expression();
        str = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        try {
            vm.getValueStack().push(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            throw new InterpreterException("invalid integer format: '" + str + "'");
        }
    }

    /**
     * Handle STRING_TO_REAL(str)
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void stringToReal() throws InterpreterException, ReturnException  {
        String str;
        tokenSequencer.expect("STRING_TO_REAL");
        tokenSequencer.expect("(");
        expression();
        str = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        try {
            vm.getValueStack().push(Double.parseDouble(str));
        } catch (NumberFormatException e) {
            throw new InterpreterException("invalid real format '" + str + "'");
        }
    }

    /**
     * Handle INT_TO_STRING(int)
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void intToString() throws InterpreterException, ReturnException  {
        int i;
        tokenSequencer.expect("INT_TO_STRING");
        tokenSequencer.expect("(");
        expression();
        i = vm.getValueStack().popInteger();
        tokenSequencer.expect(")");
        vm.getValueStack().push(Integer.toString(i));
    }

    /**
     * Handle REAL_TO_STRING(real)
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void realToString() throws InterpreterException, ReturnException  {
        double d;
        tokenSequencer.expect("REAL_TO_STRING");
        tokenSequencer.expect("(");
        expression();
        d = vm.getValueStack().popReal();
        tokenSequencer.expect(")");
        vm.getValueStack().push(Double.toString(d));
    }

    /**
     * CHAR_TO_CODE statement
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void charToCode() throws InterpreterException, ReturnException  {
        String s;
        tokenSequencer.expect("CHAR_TO_CODE");
        tokenSequencer.expect("(");
        expression();
        s = vm.getValueStack().popString();
        tokenSequencer.expect(")");
        vm.getValueStack().push((int) s.charAt(0));
    }

    /**
     * CODE_TO_CHAR statement
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void codeToChar() throws InterpreterException, ReturnException  {
        int i;
        tokenSequencer.expect("CODE_TO_CHAR");
        tokenSequencer.expect("(");
        expression();
        i = vm.getValueStack().popInteger();
        tokenSequencer.expect(")");
        vm.getValueStack().push(String.valueOf((char) i));
    }

    /**
     * USERINPUT statement
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void userInput() throws InterpreterException, ReturnException  {
        String input;
        tokenSequencer.expect("USERINPUT");
        input = inputProvider.getInput();
        vm.getValueStack().push(input.trim());
    }

    /**
     * RANDOM_INT statement
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void randomInt() throws InterpreterException, ReturnException  {
        int start, end;
        tokenSequencer.expect("RANDOM_INT");
        tokenSequencer.expect("(");
        expression();
        start = vm.getValueStack().popInteger();
        tokenSequencer.expect(",");
        expression();
        end = vm.getValueStack().popInteger();
        tokenSequencer.expect(")");
        vm.getValueStack().push(new Random().nextInt(end - start + 1) + start);
    }

    /**
     * <boolean-term> [ OR <boolean-term> ]
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void booleanExpression() throws InterpreterException, ReturnException  {
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

    /**
     * <not-factor> [ AND <not-factor> ]
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void booleanTerm() throws InterpreterException, ReturnException  {
        notFactor();
        while (tokenSequencer.match("AND") && tokenSequencer.thereIsAToken()) {
            boolean lvalue, rvalue;
            lvalue = vm.getValueStack().popBoolean();
            tokenSequencer.expect("AND");
            notFactor();
            rvalue = vm.getValueStack().popBoolean();
            if (lvalue && rvalue) {
                vm.getValueStack().push(true);
            }
            else {
                vm.getValueStack().push(false);
            }
        }
    }

    /**
     * [NOT] <relation>
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void notFactor() throws InterpreterException, ReturnException  {
        if (tokenSequencer.match("NOT")) {
            tokenSequencer.expect("NOT");
            relation();
            vm.getValueStack().push(!vm.getValueStack().popBoolean());
        } else {
            relation();
        }
    }

    /**
     * <expression> [ [comparison] <expression> ]
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void relation() throws InterpreterException, ReturnException  {
        expression();
        while (tokenSequencer.match("<") || tokenSequencer.match(">") || tokenSequencer.match("=") || tokenSequencer.match("!")) {
            Value lvalue = vm.popValue(), rvalue;
            if (tokenSequencer.match("<")) {
                tokenSequencer.advance();
                if (tokenSequencer.match("=")) {
                    tokenSequencer.advance();
                    expression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) < 1);
                } else {
                    expression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) < 0);
                }
            } else if (tokenSequencer.match(">")) {
                tokenSequencer.advance();
                if (tokenSequencer.match("=")) {
                    tokenSequencer.advance();
                    expression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) > -1);
                } else {
                    expression();
                    rvalue = vm.popValue();
                    vm.getValueStack().push(lvalue.compare(rvalue) > 0);
                }
            } else if (tokenSequencer.match("=")) {
                tokenSequencer.advance();
                expression();
                rvalue = vm.popValue();
                vm.getValueStack().push(lvalue.equal(rvalue));
            } else if (tokenSequencer.match("!")) {
                tokenSequencer.advance();
                tokenSequencer.expect("=");
                expression();
                rvalue = vm.popValue();
                vm.getValueStack().push(!lvalue.equal(rvalue));
            }
        }
    }

    /**
     * <term> [ +/- <term> ]
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void expression() throws InterpreterException, ReturnException  {
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

    /**
     * <factor> [ [ * / DIV MOD ] <factor> ]
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void term() throws InterpreterException, ReturnException  {
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

    /**
     * ( <boolean-expression> ) OR <literal>
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void factor() throws InterpreterException, ReturnException  {
        if (tokenSequencer.match("(")) {
            tokenSequencer.expect("(");
            booleanExpression();
            tokenSequencer.expect(")");
        } else {
            literal();
        }
    }

    /**
     * Literal handling:  Process
     *  actual literal values
     *  variables
     *  built in function calls
     *  user defined subroutine calls
     * 
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void literal() throws InterpreterException, ReturnException  {
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

    /**
     * Handle requests for a variable
     * @throws InterpreterException
     * @throws ReturnException 
     */
    private void variable() throws InterpreterException, ReturnException  {
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

    private void arrayLiteral() throws InterpreterException, ReturnException {
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
        if (tokenSequencer.contains(".")) {
            realLiteral();
        } else {
            integerLiteral();
        }
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
