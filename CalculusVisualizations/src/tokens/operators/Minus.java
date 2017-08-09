package tokens.operators;

/**
 * Created by janicefoley on 6/29/17.
 */
public class Minus  extends  Operator{

    public Minus() {
        super(2, false, "-");
    }

    @Override
    public double stackAction(double a, double b) {
        return b - a;
    }

}
