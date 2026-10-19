package Absyn;

public class ArrayRefExp extends Exp {
    public Exp array;
    public Exp index;

    public ArrayRefExp(int p, Exp a, Exp i) {
        pos = p;
        array = a;
        index = i;
    }
}
