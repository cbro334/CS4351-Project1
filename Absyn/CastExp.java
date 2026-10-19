package Absyn;

public class CastExp extends Exp {
    public String targetType;
    public Exp expression;

    public CastExp(int p, String type, Exp e) {
        pos = p;
        targetType = type;
        expression = e;
    }
}
