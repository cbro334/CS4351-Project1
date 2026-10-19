package Absyn;
public class ExpStm extends Stm {
    public Exp exp;

    public ExpStm(int p, Exp e) {
        pos=p; exp=e;
    }
}
