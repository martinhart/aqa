/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.parser;

/**
 * This class is used to provide a mechanism for early return from a subroutine.
 * For example, consider the following source code:
 * 
 *      SUBROUTINE exitEarly()
 *          OUTPUT 1
 *          RETURN
 *          OUTPUT 2
 *      ENDSUBROUTINE
 * 
 *      exitEarly()
 * 
 * In this code we want execution of the subroutine to stop immediately upon
 * encountering the RETURN statement.  However we may be buried deep inside
 * the recursive parser calls.
 * 
 * To accomplish the return, the parser throws a ReturnException when it sees
 * the return statement.  This is handled by the subroutine executor and causes
 * stack unwinding and replacement of the VM from the caller.
 * 
 * It is a violation of the languate to call RETURN outside of a subroutine so
 * if we weren't in a subroutine context this exception can be mapped to an
 * InterpreterException
 * 
 * @author martinhart
 */
class ReturnException extends Exception {
}
