package Absyn;
import Symbol.Symbol;

public class MemberExp extends Exp {
    public Exp target;
    public Symbol member;
    public boolean throughPointer;

    public MemberExp(int p, Exp e, Symbol m, boolean ptr) {
        pos = p;
        target = e;
        member = m;
        throughPointer = ptr;
    }
}
