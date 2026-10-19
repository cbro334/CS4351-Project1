package Absyn;

public class ProgramNode extends Absyn {
    public DecList declarations;

    public ProgramNode(int p, DecList declarations) {
        pos=p;
        this.declarations=declarations;
    }
}
