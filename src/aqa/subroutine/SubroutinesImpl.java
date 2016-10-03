/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import aqa.InterpreterException;
import java.util.HashMap;

/**
 *
 * @author martinhart
 */
public class SubroutinesImpl implements Subroutines {

    private final HashMap<String, Subroutine> subroutines;

    public SubroutinesImpl() {
        subroutines = new HashMap<>();
    }

    @Override
    public void add(Subroutine s) throws InterpreterException {
        if (subroutines.containsKey(s.getName())) {
            throw new InterpreterException("'" + s.getName() + "' already defined");
        }
        subroutines.put(s.getName(), s);
    }

    @Override
    public Subroutine get(String name) throws InterpreterException {
        Subroutine s = subroutines.get(name);
        if (s == null) {
            throw new InterpreterException("'" + name + "' is not defined");
        }
        return s;
    }
}
