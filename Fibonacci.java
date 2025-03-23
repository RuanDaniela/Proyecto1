import java.util.List;

public class Fibonacci implements LispEvaluator.LispFunction {
    @Override
    public Object apply(List<Object> args) throws LispEvaluator.EvaluatorException {
        if (args.size() != 1) {
            throw new LispEvaluator.EvaluatorException("FIBONACCI requiere exactamente un argumento.");
        }

        Object valor = args.get(0);
        if (!(valor instanceof Number)) {
            throw new LispEvaluator.EvaluatorException("FIBONACCI solo acepta números.");
        }

        int n = ((Number) valor).intValue(); // Convertimos a int correctamente
        return calcularFibonacci(n);
    }

    private int calcularFibonacci(int n) {
        if (n <= 1) return n; // Esto produce Fibonacci(0)=0, Fibonacci(1)=1
        return calcularFibonacci(n - 1) + calcularFibonacci(n - 2);
    }
}
