package tokens.bigoperations;

import java.util.List;

public class Ceiling extends BigOperation {
    public Ceiling(){
        super(8, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.ceil(inputList.get(0));
    }
}
