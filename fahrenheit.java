import java.util.List;

public class fahrenheit implements LispEvaluator.LispFunction {

    @Override
    public Object apply(List<Object> args) throws LispEvaluator.EvaluatorException {
        if (args.size() != 1) {
            throw new LispEvaluator.EvaluatorException("FAHRENHEIT requiere un solo argumento.");
        }
        Object arg = args.get(0);
        if (!(arg instanceof Number)) {
            throw new LispEvaluator.EvaluatorException("El argumento de FAHRENHEIT debe ser un número.");
        }
        // Convertir el valor a Celsius (esperamos un Double)
        Double celsius = ((Number) arg).doubleValue();
        // Realizar la conversión a Fahrenheit
        return celsius * 9.0 / 5.0 + 32;
    }
}
