package Types;

public class CHAR extends Type {
   public boolean coerceTo(Type t) {
      return t.actual() instanceof CHAR;
   }
}