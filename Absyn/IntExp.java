package Absyn;
import Symbol.Symbol;
public class IntExp extends Exp {
   public int value;
   public String lexeme;
   public IntExp(int p, int v) {pos=p; value=v;}
   public IntExp(int p, String text) {pos=p; lexeme=text;}
}
