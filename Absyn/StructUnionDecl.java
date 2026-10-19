package Absyn;
import Symbol.Symbol;

public class StructUnionDecl extends Dec {
    public ExpList modifiers;
    public String kind;
    public Symbol name;
    public DecList fields;

    public StructUnionDecl(int p, ExpList m, String k, Symbol n, DecList f) {
        pos = p;
        modifiers = m;
        kind = k;
        name = n;
        fields = f;
    }
}
