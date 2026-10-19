package Absyn;
import Symbol.Symbol;

public class TypedefDecl extends Dec {
    public String type;
    public Symbol alias;

    public TypedefDecl(int p, String t, Symbol n) {
        pos=p;
        type=t;
        alias=n;
    }
}
