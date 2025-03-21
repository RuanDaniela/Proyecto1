import java.util.*;

public class LispMain {
    public static void main(String[] args) {
        LispEvaluator evaluator = new LispEvaluator();

        try {
            // Registrar funciones externas
            evaluator.definirFuncion("celsius-a-fahrenheit", new fahrenheitLisp());
            evaluator.definirFuncion("factorial", new factorial());

            // Evaluar Celsius a Fahrenheit
            LispParser parser1 = new LispParser(Arrays.asList("(", "celsius-a-fahrenheit", "100", ")"));
            Object ast1 = parser1.parse();
            Object resultado1 = evaluator.evaluar(ast1);
            System.out.println("100°C en Fahrenheit: " + resultado1);

            // Evaluar Factorial
            LispParser parser2 = new LispParser(Arrays.asList("(", "factorial", "5", ")"));
            Object ast2 = parser2.parse();
            Object resultado2 = evaluator.evaluar(ast2);
            System.out.println("Factorial de 5: " + resultado2);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

