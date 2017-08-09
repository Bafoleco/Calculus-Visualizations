package tokens.bigoperations;

import java.util.List;

public class LogBase10 extends BigOperation{
    public LogBase10(){
        super(3, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.log10(inputList.get(0));
    }
}
