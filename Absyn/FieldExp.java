package Absyn;
public class FieldExp extends Exp {
    public String name;

    public FieldExp(int p, String name) {
        pos=p;
        this.name=name;
    }
}
