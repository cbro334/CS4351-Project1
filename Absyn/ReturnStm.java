package Absyn;
public class ReturnStm extends Stm {
    public Exp value;
    public ReturnStm(int p, Exp e) {
        pos=p; value=e;
    }
}
