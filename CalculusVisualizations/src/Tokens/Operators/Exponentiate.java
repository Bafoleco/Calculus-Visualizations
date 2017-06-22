package Tokens.Operators;

import Tokens.*;

public class Exponentiate extends Operator {

    public Exponentiate() {
        super( 4, true, "^");
    }

    @Override
    public double stackAction(double a, double b) {
        return Math.pow(b, a);
    }
    
}
