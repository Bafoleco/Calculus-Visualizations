package tokens.bigoperations;

import java.util.List;

public class Cosine extends BigOperation {

    public Cosine(){
        super(3, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.cos(inputList.get(0));
    }

}
