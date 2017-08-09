package tokens.bigoperations;

import java.util.List;

public class Floor extends BigOperation {
    public Floor(){
        super(5, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.floor(inputList.get(0));
    }
}
