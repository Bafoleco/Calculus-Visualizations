package tokens.operators;

public class Plus extends Operator {

    public Plus() {
        super( 2, false, "+");
    }

    @Override
    public double stackAction(double a, double b) {
        return a + b;
    }
}
