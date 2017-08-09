package tokens.bigoperations;

import java.util.List;

public class Tangent extends BigOperation {
    public Tangent(){
        super(4, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.tan(inputList.get(0));
    }

}
