package tokens.operators;

public class Times extends Operator {
    public Times() {
        super( 3, false, "+");
    }

    @Override
    public double stackAction(double a, double b) {
        return a * b;
    }
}
