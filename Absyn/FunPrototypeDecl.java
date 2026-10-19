package Absyn;
import Symbol.Symbol;

public class FunPrototypeDecl extends Dec {
    public ExpList modifiers;
    public String type;
    public Symbol name;
    public ExpList parameters;

    public FunPrototypeDecl(int p, ExpList mods, String t, Symbol n, ExpList params) {
        pos=p;
        modifiers=mods;
        type=t;
        name=n;
        parameters=params;
    }
}
