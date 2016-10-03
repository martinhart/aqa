/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.value.Value;

/**
 * This class represents a single argument to a subroutine.  An argument represents
 * the actual value that should be passed to the routine.
 * @author martinhart
 */
public class Argument {
    
    private final Value value;

    Argument(Value v) {
        this.value = v;
    }

    Value getValue() {
        return value;
    }
    
}
