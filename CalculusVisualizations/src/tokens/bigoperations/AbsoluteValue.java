package tokens.bigoperations;

import java.util.List;

public class AbsoluteValue extends BigOperation{
    public AbsoluteValue(){
        super(3, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.abs(inputList.get(0));
    }


}
