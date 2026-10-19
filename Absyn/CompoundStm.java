package Absyn;
public class CompoundStm extends Stm {
    public DecList declarations;
    public StmList statements;

    public CompoundStm(int p, DecList d, StmList s) {
        pos=p;
        declarations=d;
        statements=s;
    }
}
