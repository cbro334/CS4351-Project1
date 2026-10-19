package Absyn;
public class ForLoop extends Stm {
    public Stm initialization;
    public Stm condition;
    public Exp increment;
    public Stm body;

    public ForLoop(int p, Stm init, Stm check, Exp inc, Stm b) {
        pos=p;
        initialization=init;
        condition=check;
        increment=inc;
        body=b;
    }
}
