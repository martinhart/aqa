/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.value;

import aqa.InterpreterException;

/**
 * A representation of decimal numbers
 *
 * @author martinhart
 */
public class RealValue extends ValueBase {

    public final double value;

    public RealValue(double value) {
        super("real");
        this.value = value;
    }

    @Override
    public String inspect() {
        return Double.toString(value);
    }

    @Override
    public String output() {
        return inspect();
    }

    @Override
    public int compare(Value other) throws InterpreterException {
        if (other instanceof RealValue) {
            RealValue o = (RealValue) other;
            if (value < o.value) {
                return -1;
            } else if (value > o.value) {
                return 1;
            }
            return 0;
        } else if (other instanceof IntegerValue) {
            IntegerValue o = (IntegerValue) other;
            if (value < o.value) {
                return -1;
            } else if (value > o.value) {
                return 1;
            }
            return 0;
        }
        return super.compare(other);
    }

    @Override
    public boolean equal(Value other) throws InterpreterException {
        if (other instanceof RealValue) {
            RealValue o = (RealValue) other;
            return (value == o.value);
        }
        return super.equal(other);
    }

    @Override
    public Value add(Value other) throws InterpreterException {
        if (other instanceof RealValue) {
            RealValue r = (RealValue) other;
            return new RealValue(this.value + r.value);
        } else if (other instanceof IntegerValue) {
            IntegerValue i = (IntegerValue) other;
            return new RealValue(this.value + i.value);
        }
        return super.add(other);
    }

    @Override
    public Value subtract(Value other) throws InterpreterException {
        if (other instanceof RealValue) {
            RealValue r = (RealValue) other;
            return new RealValue(this.value - r.value);
        } else if (other instanceof IntegerValue) {
            IntegerValue i = (IntegerValue) other;
            return new RealValue(this.value - i.value);
        }
        return super.subtract(other);
    }

    @Override
    public Value multiply(Value other) throws InterpreterException {
        if (other instanceof RealValue) {
            RealValue r = (RealValue) other;
            return new RealValue(this.value * r.value);
        } else if (other instanceof IntegerValue) {
            IntegerValue r = (IntegerValue) other;
            return new RealValue(this.value * r.value);
        }
        return super.multiply(other);
    }

    @Override
    public Value divide(Value other) throws InterpreterException {
        if (other instanceof RealValue) {
            RealValue r = (RealValue) other;
            return new RealValue(this.value / r.value);
        } else if (other instanceof IntegerValue) {
            IntegerValue r = (IntegerValue) other;
            return new RealValue(this.value / r.value);
        }
        return super.divide(other);
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
