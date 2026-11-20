package Frame;

import Tree.Exp;

// describes where a formal parameter or local variable is stored// describes where a formal parameter or local variable is stored
public abstract class Access {
    public abstract String toString();
    public abstract Tree.Exp exp(Tree.Exp e);    
    //public abstract Exp exp(Exp framePointer);
} 
