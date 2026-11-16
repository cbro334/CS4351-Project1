package Frame;

import Tree.BINOP;
import Tree.CONST; 
import Tree.Exp;
import Tree.MEM;

// A variable stored at a byte offset from the frame pointer
public class InFrame extends Access {
    public final int offset;

    public InFrame(int offset) {
        this.offset = offset;
    }

    @Override
    public Exp exp(Exp framePointer) {
        return new MEM(new BINOP(BINOP.PLUS, framePointer, new CONST(offset))); 

    }
}