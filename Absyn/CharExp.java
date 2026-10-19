package Absyn;
public class CharExp extends Exp {
    public String value;
    public CharExp(int p, String v) {
        pos=p;
        value=v;
    }
}
