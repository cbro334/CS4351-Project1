package Absyn;
public class WhileStm extends Stm {
    public Exp condition;
    public Stm body;

    public WhileStm(int p, Exp e, Stm b) {
        pos=p; condition=e; body=b;
    }
}
