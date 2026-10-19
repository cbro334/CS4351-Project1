package Absyn;
public class DoWhileStm extends Stm {
    public Stm body;
    public Exp condition;

    public DoWhileStm(int p, Stm b, Exp e) {
        pos=p; body=b; condition=e;
    }
}
