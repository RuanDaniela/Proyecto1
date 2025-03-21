import java.util.List;

public class factorial implements LispEvaluator.LispFunction {
    @Override
    public Object apply(List<Object> args) throws LispEvaluator.EvaluatorException {
        int n = (int) (double) args.get(0); // Convertimos el argumento a int
        if (n < 0) throw new LispEvaluator.EvaluatorException("El factorial no está definido para números negativos.");
        return factorial(n);
    }

    private int factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }
}
