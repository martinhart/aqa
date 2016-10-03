/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 *
 * @author martinhart
 */
public class IntegerValue extends ValueBase {

    public final int value;

    public IntegerValue(int val) {
        super("integer");
        this.value = val;
    }

    @Override
    public String inspect() {
        return "" + value;
    }

    @Override
    public String output() {
        return "" + value;
    }

    @Override
    public int compare(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue o = (IntegerValue) other;
            if (value < o.value) {
                return -1;
            } else if (value > o.value) {
                return 1;
            }
            return 0;
        }
        else if (other instanceof RealValue) {
            RealValue o = (RealValue) other;
            if (value < o.value) {
                return -1;
            } else if (value > o.value) {
                return 1;
            }
            return 0;            
        }
        bugout("cannot compare with '" + other.inspect() + "'");
        return 0;
    }

    @Override
    public boolean equal(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue o = (IntegerValue) other;
            return (value == o.value);
        }
        return super.equal(other);
    }

    @Override
    public Value add(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue o = (IntegerValue) other;
            return new IntegerValue(this.value + o.value);
        } else if (other instanceof RealValue) {
            RealValue r = (RealValue) other;
            return new RealValue(this.value + r.value);
        } else {
            return super.add(other);
        }
    }

    @Override
    public Value subtract(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue o = (IntegerValue) other;
            return new IntegerValue(this.value - o.value);
        } else if (other instanceof RealValue) {
            RealValue r = (RealValue) other;
            return new RealValue(this.value - r.value);
        } else {
            bugout("subtract");
            return super.subtract(other);
        }
    }

    @Override
    public Value multiply(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue o = (IntegerValue) other;
            return new IntegerValue(this.value * o.value);
        } else if (other instanceof RealValue) {
            RealValue o = (RealValue) other;
            return new RealValue(this.value * o.value);
        } else {
            return super.multiply(other);
        }
    }

    @Override
    public Value divide(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue i = (IntegerValue) other;
            return new RealValue(this.value / (double) i.value);
        } else if (other instanceof RealValue) {
            RealValue r = (RealValue) other;
            return new RealValue(this.value / r.value);
        } else {
            return super.divide(other);
        }
    }

    @Override
    public Value div(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue i = (IntegerValue) other;
            return new IntegerValue(this.value / i.value);
        } else {
            return super.div(other);
        }
    }

    @Override
    public Value mod(Value other) throws InterpreterException {
        if (other instanceof IntegerValue) {
            IntegerValue i = (IntegerValue) other;
            return new IntegerValue(this.value % i.value);
        } else {
            return super.mod(other);
        }
    }

    @Override
    public Object getValue() {
        return value;
    }
    
    @Override
    public StringValue toStr() throws InterpreterException {
        return new StringValue(String.valueOf(value));
    }
}
