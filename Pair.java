
public class Pair {
    private Object first;
    private Object second;

    // Constructor
    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    // Getters
    public Object getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }

    // Método para ver si el par es un "nil" (vacío en LISP)
    public boolean isNil() {
        return this.first == null && this.second == null;
    }
}
