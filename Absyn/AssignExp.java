package Absyn;
import Symbol.Symbol;
public class AssignExp extends Exp {
   public Var var;
   public Exp exp;
   public Exp target;
   public int oper;
   public final static int ASSIGN=0, PLUS_ASSIGN=1, MINUS_ASSIGN=2, TIMES_ASSIGN=3, DIVIDE_ASSIGN=4, MOD_ASSIGN=5, LSHIFT_ASSIGN=6, RSHIFT_ASSIGN=7, AND_ASSIGN=8, XOR_ASSIGN=9, OR_ASSIGN=10;
   public AssignExp(int p, Var v, Exp e) {pos=p; var=v; exp=e;}
   public AssignExp(int p, Exp lhs, int op, Exp rhs) {pos=p; target=lhs; oper=op; exp=rhs;}
}
