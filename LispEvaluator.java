import java.util.*;
import java.util.Objects;

public class LispEvaluator {
    private Map<String, Object> environment;

    public LispEvaluator() {
        environment = new HashMap<>();
        cargarFuncionesBasicas();
        cargarFuncionesPersonalizadas();
    }

    private void cargarFuncionesBasicas() {
        // Conversión a double para mayor flexibilidad (números decimales)
        environment.put("+", (LispFunction) args -> {
            double sum = 0;
            for (Object arg : args) {
                sum += toDouble(evaluar(arg));
            }
            return sum;
        });
        environment.put("-", (LispFunction) args -> {
            if (args.isEmpty()) {
                throw new EvaluatorException("'-' requiere al menos un argumento.");
            }
            double result = toDouble(evaluar(args.get(0)));
            if (args.size() == 1) {
                return -result;
            }
            for (int i = 1; i < args.size(); i++) {
                result -= toDouble(evaluar(args.get(i)));
            }
            return result;
        });
        environment.put("*", (LispFunction) args -> {
            double result = 1;
            for (Object arg : args) {
                result *= toDouble(evaluar(arg));
            }
            return result;
        });
        environment.put("/", (LispFunction) args -> {
            if (args.size() < 2) {
                throw new EvaluatorException("'/' requiere al menos dos argumentos.");
            }
            double result = toDouble(evaluar(args.get(0)));
            for (int i = 1; i < args.size(); i++) {
                double divisor = toDouble(evaluar(args.get(i)));
                if (divisor == 0) {
                    throw new EvaluatorException("División por cero.");
                }
                result /= divisor;
            }
            return result;
        });
        environment.put(">", (LispFunction) args ->
                toDouble(evaluar(args.get(0))) > toDouble(evaluar(args.get(1))));
        environment.put("<", (LispFunction) args ->
                toDouble(evaluar(args.get(0))) < toDouble(evaluar(args.get(1))));
        environment.put("=", (LispFunction) args ->
                Objects.equals(evaluar(args.get(0)), evaluar(args.get(1))));
        environment.put("ATOM", (LispFunction) args -> {
            Object val = evaluar(args.get(0));
            return (val instanceof String) || (val instanceof Number);
        });
        environment.put("LIST", (LispFunction) args ->
                evaluar(args.get(0)) instanceof List);
        environment.put("EQUAL", (LispFunction) args ->
                Objects.equals(evaluar(args.get(0)), evaluar(args.get(1))));
    }

    private void cargarFuncionesPersonalizadas() {
        // FACTORIAL: calcula el factorial de un número (n!).
        environment.put("FACTORIAL", (LispFunction) args -> {
            if (args.size() != 1) {
                throw new EvaluatorException("FACTORIAL requiere exactamente un argumento.");
            }
            Object arg = evaluar(args.get(0));
            if (!(arg instanceof Number)) {
                throw new EvaluatorException("FACTORIAL requiere un número.");
            }
            int n = ((Number) arg).intValue();
            if (n < 0) {
                throw new EvaluatorException("FACTORIAL requiere un número no negativo.");
            }
            return factorial(n);
        });

        // FIBONACCI: calcula el n-ésimo número Fibonacci.
        environment.put("FIBONACCI", (LispFunction) args -> {
            if (args.size() != 1) {
                throw new EvaluatorException("FIBONACCI requiere exactamente un argumento.");
            }
            Object arg = evaluar(args.get(0));
            if (!(arg instanceof Number)) {
                throw new EvaluatorException("FIBONACCI requiere un número.");
            }
            int n = ((Number) arg).intValue();
            if (n < 0) {
                throw new EvaluatorException("FIBONACCI requiere un número no negativo.");
            }
            return fibonacci(n);
        });

        // FAHRENHEIT: convierte grados Celsius a Fahrenheit.
        environment.put("FAHRENHEIT", (LispFunction) args -> {
            if (args.size() != 1) {
                throw new EvaluatorException("FAHRENHEIT requiere exactamente un argumento.");
            }
            Object arg = evaluar(args.get(0));
            if (!(arg instanceof Number)) {
                throw new EvaluatorException("FAHRENHEIT requiere un número.");
            }
            double celsius = ((Number) arg).doubleValue();
            return (celsius * 9.0 / 5.0) + 32;
        });
    }

    // Función recursiva para calcular factorial.
    private int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    // Función recursiva para calcular Fibonacci.
    private int fibonacci(int n) {
        if (n == 0 || n == 1) return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    private double toDouble(Object num) throws EvaluatorException {
        if (num instanceof Number) {
            return ((Number) num).doubleValue();
        }
        throw new EvaluatorException("Se esperaba un número, pero se obtuvo: " + num);
    }

    public Object evaluar(Object ast) throws EvaluatorException {
        if (ast instanceof String) {
            String simbolo = (String) ast;
            if (environment.containsKey(simbolo)) {
                return environment.get(simbolo);
            }
            throw new EvaluatorException("Símbolo no definido: " + simbolo);
        } else if (ast instanceof Number) {
            return ast;
        } else if (ast instanceof List) {
            List<Object> list = (List<Object>) ast;
            if (list.isEmpty()) {
                throw new EvaluatorException("Error: lista vacía.");
            }

            Object primerElemento = list.get(0);
            if (!(primerElemento instanceof String)) {
                throw new EvaluatorException("El primer elemento debe ser un símbolo (nombre de función).");
            }
            String functionName = (String) primerElemento;
            // Manejo de formas especiales
            if (functionName.equals("defun")) {
                return definirFuncionLisp(list);
            } else if (functionName.equals("setq")) {
                return definirVariableLisp(list);
            } else if (functionName.equals("quote")) {
                if (list.size() != 2) {
                    throw new EvaluatorException("Error: quote necesita exactamente un argumento.");
                }
                return list.get(1);
            } else if (functionName.equals("cond")) {
                return evaluarCond(list);
            }

            Object funcionObjeto = environment.get(functionName);
            if (!(funcionObjeto instanceof LispFunction)) {
                throw new EvaluatorException("Función no definida: " + functionName);
            }
            LispFunction function = (LispFunction) funcionObjeto;
            List<Object> argumentos = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                argumentos.add(evaluar(list.get(i)));
            }
            return function.apply(argumentos);
        }
        throw new EvaluatorException("Expresión no válida.");
    }

    private Object evaluarCond(List<Object> list) throws EvaluatorException {
        for (int i = 1; i < list.size(); i++) {
            List<Object> condExp = (List<Object>) list.get(i);
            if (condExp.size() != 2) {
                throw new EvaluatorException("Cada cláusula cond debe tener exactamente 2 elementos.");
            }
            Object condition = evaluar(condExp.get(0));
            if ((condition instanceof Boolean && (Boolean) condition) ||
                    (condition instanceof Number && ((Number) condition).doubleValue() != 0)) {
                return evaluar(condExp.get(1));
            }
        }
        return null;
    }

    private Object definirFuncionLisp(List<Object> list) throws EvaluatorException {
        if (list.size() < 4) {
            throw new EvaluatorException("defun requiere al menos un nombre, parámetros y cuerpo.");
        }
        if (!(list.get(1) instanceof String)) {
            throw new EvaluatorException("El nombre de la función debe ser un símbolo.");
        }
        String nombre = (String) list.get(1);
        if (!(list.get(2) instanceof List)) {
            throw new EvaluatorException("La lista de parámetros no es válida.");
        }
        List<?> paramList = (List<?>) list.get(2);
        List<String> parametros = new ArrayList<>();
        for (Object param : paramList) {
            if (!(param instanceof String)) {
                throw new EvaluatorException("El parámetro debe ser un símbolo.");
            }
            parametros.add((String) param);
        }
        List<Object> cuerpo = list.subList(3, list.size());
        environment.put(nombre, (LispFunction) args -> {
            if (args.size() != parametros.size()) {
                throw new EvaluatorException("Cantidad incorrecta de argumentos para la función " + nombre);
            }
            Map<String, Object> localEnv = new HashMap<>(environment);
            for (int i = 0; i < parametros.size(); i++) {
                localEnv.put(parametros.get(i), args.get(i));
            }
            Object resultado = null;
            for (Object expr : cuerpo) {
                resultado = evaluarEnEntorno(expr, localEnv);
            }
            return resultado;
        });
        return nombre;
    }

    private Object definirVariableLisp(List<Object> list) throws EvaluatorException {
        if (list.size() != 3) {
            throw new EvaluatorException("setq requiere un nombre y un valor.");
        }
        if (!(list.get(1) instanceof String)) {
            throw new EvaluatorException("El nombre de la variable debe ser un símbolo.");
        }
        String nombre = (String) list.get(1);
        Object valor = evaluar(list.get(2));
        environment.put(nombre, valor);
        return valor;
    }

    private Object evaluarEnEntorno(Object ast, Map<String, Object> entorno) throws EvaluatorException {
        LispEvaluator localEvaluator = new LispEvaluator();
        localEvaluator.environment = entorno;
        return localEvaluator.evaluar(ast);
    }

    @FunctionalInterface
    public interface LispFunction {
        Object apply(List<Object> args) throws EvaluatorException;
    }

    public static class EvaluatorException extends Exception {
        public EvaluatorException(String message) {
            super(message);
        }
    }
}
