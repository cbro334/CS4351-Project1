package Absyn;
public class IfStm extends Stm {
    public Exp condition;
    public Stm thenStm;
    public Stm elseStm;

    public IfStm(int p, Exp e, Stm t, Stm f) {
        pos=p;
        condition=e;
        thenStm=t;
        elseStm=f;
    }
}
