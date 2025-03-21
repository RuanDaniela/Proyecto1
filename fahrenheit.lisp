import java.util.List;

public class fahrenheitLisp implements LispEvaluator.LispFunction {
    @Override
    public Object apply(List<Object> args) throws LispEvaluator.EvaluatorException {
        double celsius = (double) args.get(0);
        return (celsius * 9 / 5) + 32;
    }
}


