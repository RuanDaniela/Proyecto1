import java.util.*;

public class LispMain {
    public static void main(String[] args) {
        LispEvaluator evaluator = new LispEvaluator();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenido al intérprete de LISP. Escribe una expresión para evaluar o 'salir' para terminar.");
        while (true) {
            System.out.print("LISP> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) break;

            try {
                List<String> tokens = Arrays.asList(input.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+"));
                LispParser parser = new LispParser(tokens);
                Object ast = parser.parse();
                Object resultado = evaluator.evaluar(ast);
                System.out.println("Resultado: " + resultado);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Intérprete finalizado.");
    }
}
