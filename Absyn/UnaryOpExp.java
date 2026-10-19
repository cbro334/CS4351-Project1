package Absyn;

public class UnaryOpExp extends Exp {
    public static final int POST_DEC = 0, POST_INC = 1,
        ADDR_OF = 2, DEREF = 3, NOT = 4, POS = 5,
        NEG = 6, PRE_DEC = 7, PRE_INC = 8, BIT_NOT = 9;

    public int oper;
    public Exp exp;

    public UnaryOpExp(int p, int o, Exp e) {
        pos = p;
        oper = o;
        exp = e;
    }
}
