import java.util.*;
import java.util.regex.*;

public class LispLexer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        String line;

        System.out.println("Introduce tu código LISP (termina con una línea vacía):");

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.isEmpty()) break;
            input.append(line).append(" ");
        }
        scanner.close();

        // Verificar paréntesis balanceados
        if (!parentesisBalanceados(input.toString())) {
            System.out.println("La expresión es INCORRECTA (problema con los paréntesis).");
            return;
        }
        System.out.println("¡La expresión es CORRECTA (paréntesis balanceados)!");

        // Tokenizar y manejar errores
        try {
            List<String> tokens = dividirEnTokens(input.toString());
            System.out.println("Tokens encontrados: " + tokens);
        } catch (LexerException e) {
            System.out.println("Error léxico: " + e.getMessage());
        }
    }

    public static boolean parentesisBalanceados(String expr) {
        int contador = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(') contador++;
            else if (c == ')') contador--;
            if (contador < 0) return false;
        }
        return contador == 0;
    }

    public static List<String> dividirEnTokens(String expr) throws LexerException {
        List<String> tokens = new ArrayList<>();
        // Soporta números negativos, decimales, notación científica, símbolos con caracteres especiales
        Pattern pattern = Pattern.compile("\\(|\\)|-?\\d+(\\.\\d+)?(e[+-]?\\d+)?|[\\w+\\-*/!?=<>.]+|\\S");
        Matcher matcher = pattern.matcher(expr.trim());

        while (matcher.find()) {
            String token = matcher.group();
            if (!token.matches("\\(|\\)|-?\\d+(\\.\\d+)?(e[+-]?\\d+)?|[\\w+\\-*/!?=<>.]+")) {
                throw new LexerException("Carácter no válido encontrado: " + token);
            }
            tokens.add(token);
        }
        return tokens;
    }

    static class LexerException extends Exception {
        public LexerException(String message) {
            super(message);
        }
    }
}