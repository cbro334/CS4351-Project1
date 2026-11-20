package Frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Temp.label;

public abstract class Frame {
    private label name;
    private List<Access> formalAccesses;

    protected Frame(label name, List<Access> formals) {
        if (name == null) {
            throw new IllegalArgumentException("Frame name cannot be null");
        }
        if (formals == null) {
            throw new IllegalArgumentException("formals cannot be null");
        }
        this.name = name;
        this.formalAccesses = new ArrayList<>(formals);
    }

    public label name() {
        return name;
    }

    public List<Access> formals() {
        return Collections.unmodifiableList(formalAccesses);
    }

    public abstract Access allocLocal(boolean escape);

    //Intermediate Code: Skeleton Code
    //public Temp.Label name;
    //public AccessList formals;
    //abstract public Frame newFrame(Symbol.Symbol name, Util.BoolList formals);
    //abstract public Access allocLocal(boolean escape);
    //abstract public Temp.Temp FP();
    //abstract public int wordSize();
    //abstract public Tree.Exp externalCall(String func, Tree.ExpList args);
    //abstract public Temp.Temp RV();
    //abstract public Tree.Stm procEntryExit1(Tree.Stm body);
    //abstract public String string(Temp.Label label, String value);
    //abstract public Temp.Label badPtr();
    //abstract public Temp.Label badSub();   
}
