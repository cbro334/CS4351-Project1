package Absyn;
import Symbol.Symbol;

public class FunDecl extends Dec {
    public ExpList modifiers;
    public String type;
    public Symbol name;
    public ExpList parameters;
    public Stm body;

    public FunDecl(int p, ExpList modifiers, String type,
                   Symbol name, ExpList parameters, Stm body) {
        pos=p;
        this.modifiers=modifiers;
        this.type=type;
        this.name=name;
        this.parameters=parameters;
        this.body=body;
    }
}
