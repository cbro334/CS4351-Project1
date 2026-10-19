package Absyn;
import Symbol.Symbol;

public class VarDecl extends Dec {
    public ExpList modifiers;
    public String type;
    public Symbol name;
    public Exp initializer;

    public VarDecl(int p, ExpList modifiers, String type,
                   Symbol name, Exp initializer) {
        pos=p;
        this.modifiers=modifiers;
        this.type=type;
        this.name=name;
        this.initializer=initializer;
    }
}
