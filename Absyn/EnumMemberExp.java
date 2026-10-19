package Absyn;
import Symbol.Symbol;

public class EnumMemberExp extends Exp {
    public Symbol name;
    public Exp value;

    public EnumMemberExp(int p, Symbol n, Exp v) {
        pos = p;
        name = n;
        value = v;
    }
}
