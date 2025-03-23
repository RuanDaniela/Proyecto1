import java.util.*;
import java.io.*;

public class LispMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al intérprete de LISP.");
        System.out.println("Comandos:");
        System.out.println("  'salir'              -> Termina el intérprete.");
        System.out.println("  'archivo <ruta>'     -> Carga un archivo de código LISP.");
        System.out.println("  O bien, ingresa una expresión LISP directamente.");
        System.out.println();

        LispEvaluator evaluator = new LispEvaluator();

        while (true) {
            System.out.print("LISP> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir"))
                break;

            if (input.toLowerCase().startsWith("archivo ")) {
                String[] parts = input.split("\\s+", 2);
                if (parts.length < 2) {
                    System.out.println("Debe especificar la ruta del archivo.");
                    continue;
                }
                try {
                    input = leerArchivo(parts[1]);
                } catch (IOException e) {
                    System.out.println("Error leyendo el archivo: " + e.getMessage());
                    continue;
                }
            }

            if (!LispLexer.parentesisBalanceados(input)) {
                System.out.println("La expresión es INCORRECTA: paréntesis desbalanceados.");
                continue;
            }

            try {
                List<String> tokens = LispLexer.dividirEnTokens(input);
                LispParser parser = new LispParser(tokens);
                Object ast = parser.parse();
                // Se comenta la impresión de tokens y AST para el usuario final.
                // System.out.println("Tokens encontrados: " + tokens);
                // System.out.println("AST generado: " + ast);
                Object resultado = evaluator.evaluar(ast);
                System.out.println("Resultado: " + resultado);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        System.out.println("Intérprete finalizado.");
        scanner.close();
    }

    private static String leerArchivo(String ruta) throws IOException {
        StringBuilder contenido = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = br.readLine()) != null) {
            contenido.append(linea).append(" ");
        }
        br.close();
        return contenido.toString().trim();
    }
}
