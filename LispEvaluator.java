import java.util.*;

public class LispEvaluator {
    private Map<String, Object> environment;

    public LispEvaluator() {
        environment = new HashMap<>();
        cargarFuncionesBasicas();
    }

    private void cargarFuncionesBasicas() {
        // Definimos funciones básicas en el entorno
        environment.put("+", (LispFunction) args -> (double) args.get(0) + (double) args.get(1));
        environment.put("-", (LispFunction) args -> (double) args.get(0) - (double) args.get(1));
        environment.put("*", (LispFunction) args -> (double) args.get(0) * (double) args.get(1));
        environment.put("/", (LispFunction) args -> (double) args.get(0) / (double) args.get(1));
        environment.put(">", (LispFunction) args -> (double) args.get(0) > (double) args.get(1));
        environment.put("<", (LispFunction) args -> (double) args.get(0) < (double) args.get(1));
        environment.put("=", (LispFunction) args -> Objects.equals(args.get(0), args.get(1)));
    }

    public Object evaluar(Object ast) throws EvaluatorException {
        if (ast instanceof String) {
            String simbolo = (String) ast;
            if (environment.containsKey(simbolo)) {
                return environment.get(simbolo);
            }
            throw new EvaluatorException("Símbolo no definido: " + simbolo);
        } else if (ast instanceof Number) {
            return ast; // Retornar números tal cual
        } else if (ast instanceof List) {
            List<Object> list = (List<Object>) ast;
            if (list.isEmpty()) {
                throw new EvaluatorException("Error: lista vacía.");
            }

            Object first = list.get(0);
            if (!(first instanceof String)) {
                throw new EvaluatorException("Error: operador inválido.");
            }

            String functionName = (String) first;
            if (!environment.containsKey(functionName)) {
                throw new EvaluatorException("Función no definida: " + functionName);
            }

            LispFunction function = (LispFunction) environment.get(functionName);
            List<Object> argumentos = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                argumentos.add(evaluar(list.get(i))); // Evaluar argumentos antes de pasarlos a la función
            }
            return function.apply(argumentos);
        }
        throw new EvaluatorException("Expresión no válida.");
    }

    @FunctionalInterface
    interface LispFunction {
        Object apply(List<Object> args) throws EvaluatorException;
    }

    static class EvaluatorException extends Exception {
        public EvaluatorException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        LispEvaluator evaluator = new LispEvaluator();
        LispParser parser = new LispParser(Arrays.asList("(", "+", "3", "5", ")")); // Ejemplo de expresión (+ 3 5)
        try {
            Object ast = parser.parse();
            Object resultado = evaluator.evaluar(ast);
            System.out.println("Resultado: " + resultado);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
