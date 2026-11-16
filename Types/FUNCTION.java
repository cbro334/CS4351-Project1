package Types;

public class FUNCTION extends Type {
   public RECORD formals;
   public Type result;
   public FUNCTION(RECORD f, Type r) {formals = f; result = r;}
}