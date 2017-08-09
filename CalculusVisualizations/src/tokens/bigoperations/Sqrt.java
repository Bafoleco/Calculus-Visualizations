package tokens.bigoperations;

import java.util.List;

public class Sqrt extends BigOperation{
    public Sqrt(){
        super(3, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.sqrt(inputList.get(0));
    }

}
