package Tokens.Operators;

public class Divide extends Operator {
    public Divide() {
        super( 3, false, "+");
    }

    @Override
    public double stackAction(double a, double b) {
        return b / a;
    }
}
