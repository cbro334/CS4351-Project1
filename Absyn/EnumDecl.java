package Absyn;
import Symbol.Symbol;

public class EnumDecl extends Dec {
    public Symbol name;
    public ExpList members;

    public EnumDecl(int p, Symbol n, ExpList m) {
        pos = p;
        name = n;
        members = m;
    }
}
