package Absyn;
public class ParenExp extends Exp {
    public Exp inner;
    public ParenExp(int p, Exp e) {
        pos=p;
        inner=e;
    }
}
