package Absyn;
import Symbol.Symbol;
public class CallExp extends Exp {
   public Symbol func;
   public Exp callee;
   public ExpList args;
   public CallExp(int p, Symbol f, ExpList a) {pos=p; func=f; args=a;}
   public CallExp(int p, Exp f, ExpList a) {pos=p; callee=f; args=a;}
}
