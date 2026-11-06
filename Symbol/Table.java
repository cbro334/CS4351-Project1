package Symbol;
import java.util.HashMap;
public class Table {
    private HashMap<Symbol, Object> dict = new HashMap<>();
    private Binder top;
    public void put(Symbol key, Object value) {
        dict.put(key, value);
        top = new Binder(key, dict.get(key), top);
    }
    public Object get(Symbol key) { return dict.get(key); }
    public void beginScope() { top = new Binder(null, null, top); }
    public void endScope() {
        while (top != null && top.key != null) {
            dict.put(top.key, top.prevtop);
            top = top.tail;
        }
        if (top != null) { top = top.tail; }
    }
}
class Binder {
    Symbol key; Object value; Object prevtop; Binder tail;
    Binder(Symbol k, Object v, Binder t) { key = k; value = v; tail = t; }
}
