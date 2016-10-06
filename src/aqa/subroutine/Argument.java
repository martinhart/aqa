/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.value.Value;

/**
 * This class represents a single argument to a subroutine.
 * 
 * For example:
 * 
 * SUBROUTINE add(a, b)
 *      RETURN a+b
 * ENDSUBROUTINE
 * 
 * add(1, 2)
 * 
 * In the above code, when the parser is executing the function call, it needs
 * to provide the value 1 as an argument to the subroutine.  That is done
 * using this class.
 * 
 * TODO: refactoring - currently the parser does not use this class.  Either it
 * should or we need to remove it.
 * 
 * @author martinhart
 */
public class Argument {
    
    /**
     * The value of the subroutine argument
     */
    private final Value value;

    /**
     * Create a new argument
     * @param v value of argument
     */
    Argument(Value v) {
        this.value = v;
    }

    /**
     * Query the value of the argument
     * @return the value
     */
    Value getValue() {
        return value;
    }    
}
