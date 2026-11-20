package Frame;

import Tree.Exp;

// describes where a formal parameter or local variable is stored// describes where a formal parameter or local variable is stored
public abstract class Access {
    public Temp.Label name;
    public AccessList formals;
    abstract public Frame newFrame(Symbol.Sybol name, Util.BoolList formals);
    abstract public Access allocLocal(boolean escape);
    abstract public Temp.Temp FP();
    abstract public int wordSize();
    abstract public Tree.Exp externalCall(String func, Tree.ExpList args);
    abstract public Temp.Temp RV();
    abstract public Tree.Stm procEntryExit1(Tree.Stm body);
    abstract public String string(Temp.Label label, String value);
    abstract public Temp.Label badPtr();
    abstract public Temp.Label badSub();
    
    public abstract Exp exp(Exp framePointer);
} 
