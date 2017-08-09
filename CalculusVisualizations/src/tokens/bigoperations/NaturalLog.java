package tokens.bigoperations;
import java.util.List;

public class NaturalLog extends BigOperation{
    public NaturalLog(){
        super(2, 1);
    }

    @Override
    public double stackAction(List<Double> inputList) {
        return Math.log(inputList.get(0));
    }

}
