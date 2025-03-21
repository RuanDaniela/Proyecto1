import java.util.*;

public class LispEvaluator {
    private Map<String, Object> environment;

    public LispEvaluator() {
        environment = new HashMap<>();
        cargarFuncionesBasicas();
    }

    private void cargarFuncionesBasicas() {
        environment.put("+", (LispFunction) args -> (double) args.get(0) + (double) args.get(1));
        environment.put("-", (LispFunction) args -> (double) args.get(0) - (double) args.get(1));
        environment.put("*", (LispFunction) args -> (double) args.get(0) * (double) args.get(1));
        environment.put("/", (LispFunction) args -> (double) args.get(0) / (double) args.get(1));
        environment.put(">", (LispFunction) args -> (double) args.get(0) > (double) args.get(1));
        environment.put("<", (LispFunction) args -> (double) args.get(0) < (double) args.get(1));
        environment.put("=", (LispFunction) args -> Objects.equals(args.get(0), args.get(1)));
        environment.put("ATOM", (LispFunction) args -> args.get(0) instanceof String || args.get(0) instanceof Number);
        environment.put("LIST", (LispFunction) args -> args.get(0) instanceof List);
        environment.put("EQUAL", (LispFunction) args -> Objects.equals(args.get(0), args.get(1)));
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

            String functionName = (String) list.get(0);
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
            if (evaluar(condExp.get(0)).equals(true)) {
                return evaluar(condExp.get(1));
            }
        }
        return null;
    }

    private Object definirFuncionLisp(List<Object> list) throws EvaluatorException {
        if (list.size() < 4) {
            throw new EvaluatorException("defun requiere al menos un nombre, parámetros y cuerpo.");
        }

        String nombre = (String) list.get(1);
        List<String> parametros = (List<String>) list.get(2);
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
    interface LispFunction {
        Object apply(List<Object> args) throws EvaluatorException;
    }

    static class EvaluatorException extends Exception {
        public EvaluatorException(String message) {
            super(message);
        }
    }
}
