package tokens.operators;

import tokens.Token;

public abstract class Operator extends Token {
    int precedence;
    //true = right, false = left
    boolean associativity;
    String appearance;

    public Operator(int precedence, boolean associativity, String appearance) {
        this.precedence = precedence;
        this.associativity = associativity;
        this.appearance = appearance;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isRightAssociative() {
        return associativity;
    }

    public abstract double stackAction(double a, double b);
}
