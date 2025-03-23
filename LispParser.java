import java.util.*;

public class LispParser {
    private List<String> tokens;
    private int index;

    public LispParser(List<String> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    // Método recursivo para construir el AST a partir de los tokens.
    public Object parse() {
        if (index >= tokens.size()) {
            throw new RuntimeException("Error: expresión inesperada al final.");
        }

        String token = tokens.get(index);
        index++;

        if (token.equals("(")) {
            List<Object> list = new ArrayList<>();
            while (index < tokens.size() && !tokens.get(index).equals(")")) {
                list.add(parse());
            }
            if (index >= tokens.size() || !tokens.get(index).equals(")")) {
                throw new RuntimeException("Error: paréntesis desbalanceados.");
            }
            index++; // Consumir el ')'
            return list;
        } else if (token.equals(")")) {
            throw new RuntimeException("Error: paréntesis inesperado.");
        } else {
            // Intentamos interpretar números; si falla, se deja como símbolo (String)
            try {
                return Integer.parseInt(token);
            } catch (NumberFormatException e) {
                return token;
            }
        }
    }
}
