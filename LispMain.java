import java.util.*;
import java.io.*;

public class LispMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al intérprete de LISP.");
        System.out.println("Comandos: ");
        System.out.println("  'salir'              -> Termina el intérprete.");
        System.out.println("  'archivo <ruta>'     -> Carga un archivo de código LISP.");
        System.out.println("  O bien, ingresa una expresión LISP directamente.");
        System.out.println();

        while (true) {
            System.out.print("LISP> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) break;

            // Si el usuario desea cargar un archivo.
            if (input.toLowerCase().startsWith("archivo ")) {
                String[] parts = input.split("\\s+", 2);
                if (parts.length < 2) {
                    System.out.println("Debe especificar la ruta del archivo.");
                    continue;
                }
                try {
                    input = leerArchivo(parts[1]);
                    System.out.println("Archivo leído:");
                    System.out.println(input);
                } catch (IOException e) {
                    System.out.println("Error leyendo el archivo: " + e.getMessage());
                    continue;
                }
            }

            // Verificamos que los paréntesis estén balanceados.
            if (!LispLexer.parentesisBalanceados(input)) {
                System.out.println("La expresión es INCORRECTA: paréntesis desbalanceados.");
                continue;
            }

            try {
                // Usamos el método dividirEnTokens del lexer.
                List<String> tokens = LispLexer.dividirEnTokens(input);
                System.out.println("Tokens encontrados: " + tokens);
                LispParser parser = new LispParser(tokens);
                Object ast = parser.parse();
                System.out.println("AST generado: " + ast);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        System.out.println("Intérprete finalizado.");
        scanner.close();
    }

    // Método para leer el contenido de un archivo y retornarlo como cadena.
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
