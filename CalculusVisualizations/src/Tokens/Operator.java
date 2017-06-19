package Tokens;

import Tokens.Token;

import java.util.List;

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

    public boolean isAssociativity() {
        return associativity;
    }

    public abstract void stackAction(List<Token> tokenList);
}
