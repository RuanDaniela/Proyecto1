import java.util.*;
import java.util.regex.*;

public class LispLexer {
    // Precompilamos el patrón para optimizar la tokenización.
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\(|\\)|-?\\d+(\\.\\d+)?(e[+-]?\\d+)?|\"(\\\\.|[^\"])*\"|[\\w+\\-*/!?=<>.]+"
    );

    // Método para dividir en tokens usando la expresión regular.
    public static List<String> dividirEnTokens(String expr) throws LexerException {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(expr.trim());
        int lastMatchEnd = 0;
        while (matcher.find()) {
            if (matcher.start() != lastMatchEnd) {
                // Si hay caracteres entre tokens (no solo espacios), se lanza error.
                String skipped = expr.substring(lastMatchEnd, matcher.start());
                if (!skipped.trim().isEmpty()) {
                    throw new LexerException("Carácter no válido encontrado: " + skipped.trim());
                }
            }
            tokens.add(matcher.group());
            lastMatchEnd = matcher.end();
        }
        if (lastMatchEnd != expr.length()) {
            String trailing = expr.substring(lastMatchEnd);
            if (!trailing.trim().isEmpty()) {
                throw new LexerException("Carácter no válido encontrado: " + trailing.trim());
            }
        }
        return tokens;
    }

    // Método para verificar que los paréntesis estén balanceados.
    public static boolean parentesisBalanceados(String expr) {
        int contador = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(') contador++;
            else if (c == ')') contador--;
            if (contador < 0) return false;
        }
        return contador == 0;
    }

    public static class LexerException extends Exception {
        public LexerException(String message) {
            super(message);
        }
    }
}
