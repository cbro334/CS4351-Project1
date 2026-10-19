package Absyn;

public class CondExp extends Exp {
    public Exp condition;
    public Exp thenExp;
    public Exp elseExp;

    public CondExp(int p, Exp c, Exp t, Exp f) {
        pos = p;
        condition = c;
        thenExp = t;
        elseExp = f;
    }
}
