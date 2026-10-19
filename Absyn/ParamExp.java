package Absyn;
public class ParamExp extends Exp {
    public String type;
    public String name;

    public ParamExp(int p, String type, String name) {
        pos=p;
        this.type=type;
        this.name=name;
    }
}
