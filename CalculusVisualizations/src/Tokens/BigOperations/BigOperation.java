package Tokens.BigOperations;

import Tokens.Token;

import java.util.List;

public abstract class BigOperation extends Token {
    int symbolLength;
    int argsTaken;

    public BigOperation(int symbolLength, int argsTaken) {
        this.symbolLength = symbolLength;
        this.argsTaken = argsTaken;
    }

    public abstract double stackAction(List<Double> inputList);

    public int getArgsTaken() {
        return argsTaken;
    }
}
