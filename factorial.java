import java.util.List;

public class factorial implements LispEvaluator.LispFunction {
    @Override
    public Object apply(List<Object> args) throws LispEvaluator.EvaluatorException {
        if (args.size() != 1) {
            throw new LispEvaluator.EvaluatorException("FACTORIAL requiere exactamente un argumento.");
        }

        Object valor = args.get(0);
        if (!(valor instanceof Number)) {
            throw new LispEvaluator.EvaluatorException("FACTORIAL solo acepta números.");
        }

        int n = ((Number) valor).intValue(); // Convertimos a int correctamente
        if (n < 0) {
            throw new LispEvaluator.EvaluatorException("El factorial no está definido para números negativos.");
        }

        return calcularFactorial(n);
    }

    private int calcularFactorial(int n) {
        if (n == 0) return 1;
        return n * calcularFactorial(n - 1);
    }
}
