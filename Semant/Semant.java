package Semant;
import Translate.Exp;
import Types.Type;

public class Semant {
  Env env;
  public Semant(ErrorMsg.ErrorMsg err) {
    this(new Env(err));
  }
  Semant(Env e) {
    env = e;
  }

  public void transProg(Absyn.Exp exp) {
    transExp(exp);
  }

  private void error(int pos, String msg) {
    env.errorMsg.error(pos, msg);
  }

  static final Types.VOID   VOID   = new Types.VOID();
  static final Types.INT    INT    = new Types.INT();
  static final Types.STRING STRING = new Types.STRING();
  static final Types.NIL    NIL    = new Types.NIL();

  private Exp checkInt(ExpTy et, int pos) {
    if (!INT.coerceTo(et.ty))
      error(pos, "integer required");
    return et.exp;
  }

  ExpTy transExp(Absyn.Exp e) {
          ExpTy result;

          if (e == null)
                  return new ExpTy(null, VOID);
          else if (e instanceof Absyn.ArrayExp)
                  result = transExp((Absyn.ArrayExp)e);
          else if (e instanceof Absyn.ArrayInitExp)
                  result = transExp((Absyn.ArrayInitExp)e);
          else if (e instanceof Absyn.ArrayRefExp)
                  result = transExp((Absyn.ArrayRefExp)e);
          else if (e instanceof Absyn.AssignExp)
                  result = transExp((Absyn.AssignExp)e);
          else if (e instanceof Absyn.BreakExp)
                  result = transExp((Absyn.BreakExp)e);
          else if (e instanceof Absyn.CallExp)
                  result = transExp((Absyn.CallExp)e);
          else if (e instanceof Absyn.CastExp)
                  result = transExp((Absyn.CastExp)e);
          else if (e instanceof Absyn.CharExp)
                  result = transExp((Absyn.CharExp)e);
          else if (e instanceof Absyn.CondExp)
                  result = transExp((Absyn.CondExp)e);
          else if (e instanceof Absyn.EnumMemberExp)
                  result = transExp((Absyn.EnumMemberExp)e);
          else if (e instanceof Absyn.FieldExp)
                  result = transExp((Absyn.FieldExp)e);
          else if (e instanceof Absyn.ForExp)
                  result = transExp((Absyn.ForExp)e);
          else if (e instanceof Absyn.IfExp)
                  result = transExp((Absyn.IfExp)e);
          else if (e instanceof Absyn.IntExp)
                  result = transExp((Absyn.IntExp)e);
          else if (e instanceof Absyn.LetExp)
                  result = transExp((Absyn.LetExp)e);
          else if (e instanceof Absyn.MemberExp)
                  result = transExp((Absyn.MemberExp)e);
          else if (e instanceof Absyn.NilExp)
                  result = transExp((Absyn.NilExp)e);
          else if (e instanceof Absyn.OpExp)
                  result = transExp((Absyn.OpExp)e);
          else if (e instanceof Absyn.ParamExp)
                  result = transExp((Absyn.ParamExp)e);
          else if (e instanceof Absyn.ParenExp)
                  result = transExp((Absyn.ParenExp)e);
          else if (e instanceof Absyn.RecordExp)
                  result = transExp((Absyn.RecordExp)e);
          else if (e instanceof Absyn.SeqExp)
                  result = transExp((Absyn.SeqExp)e);
          else if (e instanceof Absyn.SizeofExp)
                  result = transExp((Absyn.SizeofExp)e);
          else if (e instanceof Absyn.StringExp)
                  result = transExp((Absyn.StringExp)e);
          else if (e instanceof Absyn.UnaryOpExp)
                  result = transExp((Absyn.UnaryOpExp)e);
          else if (e instanceof Absyn.VarExp)
                  result = transExp((Absyn.VarExp)e);
          else if (e instanceof Absyn.WhileExp)
                  result = transExp((Absyn.WhileExp)e);
          else throw new Error("Semant.transExp");
          e.type = result.ty;
          return result;
  }

  //TODO: Create translate functions for Absyn classes

  ExpTy transExp(Absyn.OpExp e) {
    ExpTy left = transExp(e.left);
    ExpTy right = transExp(e.right);

    switch (e.oper) {
    case Absyn.OpExp.PLUS:
      checkInt(left, e.left.pos);
      checkInt(right, e.right.pos);
      return new ExpTy(null, INT);
    default:
      throw new Error("unknown operator");
    }
  }

  ExpTy transExp(Absyn.LetExp e) {
    env.venv.beginScope();
    env.tenv.beginScope();
    for (Absyn.DecList d = e.decs; d != null; d = d.tail) {
      transDec(d.head);
    }
    ExpTy body = transExp(e.body);
    env.venv.endScope();
    env.tenv.endScope();
    return new ExpTy(null, body.ty);
  }

  //Declaration Dispatch
  ExpTy transDec(Absyn.Dec d) {
          ExpTy result;

          if (d instanceof Absyn.VarDec)
                  return transDec((Absyn.VarDec)d);
          if (d instanceof Absyn.VarDecl)
                  return transDec((Absyn.VarDecl)d);
          if (d instanceof Absyn.FunctionDec)
                  return transDec((Absyn.FunctionDec)d);
          if (d instanceof Absyn.FunDecl)
                  return transDec((Absyn.FunDecl)d);
          if (d instanceof Absyn.FunPrototypeDecl)
                  return transDec((Absyn.FunPrototypeDecl)d);
          if (d instanceof Absyn.TypeDec)
                  return transDec((Absyn.TypeDec)d);
          if (d instanceof Absyn.TypedefDecl)
                  return transDec((Absyn.TypedefDecl)d);
          if (d instanceof Absyn.StructUnionDecl)
                  return transDec((Absyn.StructUnionDecl)d);
          if (d instanceof Absyn.EnumDecl)
                  return transDec((Absyn.EnumDecl)d);
          else throw new Error("Semant.transDec");
          d.type = result.ty;
          return result;
  }

  //TODO: Create translate functions for Absyn classes

  Exp transDec(Absyn.Dec d) {
    if (d instanceof Absyn.VarDec)
      return transDec((Absyn.VarDec)d);
    throw new Error("Semant.transDec");
  }

  Exp transDec(Absyn.VarDec d) {
    // NOTE: THIS IMPLEMENTATION IS INCOMPLETE
    // It is here to show you the general form of the transDec methods
    ExpTy init = transExp(d.init);
    Type type;
    if (d.typ == null) {
      type = init.ty;
    } else {
      type = VOID;
      throw new Error("unimplemented");
    }
    d.entry = new VarEntry(type);
    env.venv.put(d.name, d.entry);
    return null;
  }

  //Statement Dispatch
  ExpTy transStm(Absyn.Stm s) {
          ExpTy result;

          if (s instanceof Absyn.BreakStm)
                  return transStm((Absyn.BreakStm)s);
          if (s instanceof Absyn.CompoundStm)
                  return transStm((Absyn.CompoundStm)s);
          if (s instanceof Absyn.ContinueStm)
                  return transStm((Absyn.ContinueStm)s);
          if (s instanceof Absyn.DoWhileStm)
                  return transStm((Absyn.DoWhileStm)s);
          if (s instanceof Absyn.ExpStm)
                  return transStm((Absyn.ExpStm)s);
          if (s instanceof Absyn.IfStm)
                  return transStm((Absyn.IfStm)s);
          if (s instanceof Absyn.ReturnStm)
                  return transStm((Absyn.ReturnStm)s);
          if (s instanceof Absyn.StmList)
                  return transStm((Absyn.StmList)s);
          if (s instanceof Absyn.WhileStm)
                  return transStm((Absyn.WhileStm)s);
          else throw new Error("Semant.transStm");
          s.type = result.ty;
          return result;
  }

  //TODO: Create translate functions for Absyn classes
