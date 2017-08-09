package Tokens;

import Tokens.Token;

public class Constant extends Token {

    private double value;

    public Constant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double d) {
        value = d;
    }
}
