package tokens.bigoperations;

import java.util.List;

public class Sine extends BigOperation {

    public Sine(){
        super(3, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.sin(inputList.get(0));
    }


}
