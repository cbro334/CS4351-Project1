package Absyn;

public class SizeofExp extends Exp {
    public Exp expression;
    public String type;

    public SizeofExp(int p, Exp e, String t) {
        pos = p;
        expression = e;
        type = t;
    }
}
