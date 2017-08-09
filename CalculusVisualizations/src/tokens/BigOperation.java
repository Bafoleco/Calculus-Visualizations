package Tokens;

import Tokens.Token;

import java.util.List;

public abstract class BigOperation extends Token {
    int symbolLength;
    int argsTaken;

    public BigOperation(int symbolLength, int argsTaken) {
        this.symbolLength = symbolLength;
        this.argsTaken = argsTaken;
    }

    public abstract void stackAction(List<Token> tokenList);
}
