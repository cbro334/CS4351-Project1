package Absyn;

public class ArrayInitExp extends Exp {
    public ExpList elements;

    public ArrayInitExp(int p, ExpList elems) {
        pos = p;
        elements = elems;
    }
}
