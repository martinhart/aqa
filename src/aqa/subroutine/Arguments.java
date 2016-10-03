/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.subroutine;

import java.util.ArrayList;

/**
 * This class provides a set of arguments to a subroutine.
 *
 * @author martinhart
 */
public class Arguments {

    private final ArrayList<Argument> arguments;

    public Arguments() {
        arguments = new ArrayList<>();
    }

    public int length() {
        return arguments.size();
    }

    void add(Argument a) {
        arguments.add(a);
    }

    Argument get(int i) throws ArgumentException {
        try {
            return arguments.get(i);
        } catch (IndexOutOfBoundsException e) {
            throw new ArgumentException("No argument at index " + i);
        }
    }

}
