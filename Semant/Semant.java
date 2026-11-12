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

  //Expression Dispatch
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

  //ArrayExp Translate
  ExpTy transExp(Absyn.ArrayExp e) {
          //Look up and resolve the array type
          Types.Type t = (Types.Type) env.tenv.get(e.typ);
          if (t == null) {
                  error(e.pos, "undefined type: " + e.typ);
                  return new ExpTy(null, VOID);
          }
          Types.Type actual = t.actual();
          if (!(actual instanceof Types.ARRAY)) {
                  error(e.pos, "array type required: " + e.typ);
                  return new ExpTy(null, VOID);
          }
          //Verify array size is a number
          ExpTy size = transExp(e.size);
          checkInt(size, e.size.pos);
          //Enforce list initialization when present
          Types.ARRAY arrayType = (Types.ARRAY) actual;
          if (e.init != null) {
                  if (!(e.init instanceof Absyn.ArrayInitExp)) {
                          error(e.init.pos, "array must be initialized with a list");
                  }else{
                          //Validate that every element is of arrayType
                          checkArrayInitList((Absyn.ArrayInitExp) e.init, arrayType.element);
                  }
          }
          return new ExpTy(null, actual);
  }

  //ArrayInitExp Translate
  ExpTy transExp(Absyn.ArrayInitExp e) {
          if (e.elements == null) {
                  return new ExpTy(null, VOID);
          }
          //Find type
          ExpTy first = transExp(e.elements.head);
          Types.Type elemType = first.ty;
          //Validate types for remainder of list
          for (Absyn.ExpList cur = e.elements.tail; cur != null; cur = cur.tail) {
                  ExpTy next = transExp(cur.head);
                  if (!isAssignable(elemType, next.ty)) {
                          error(cur.head.pos, "incompatible element type");
                  }
          }
  }
  //ArrayRefExp Translate
  ExpTy transExp(Absyn.ArrayRefExp e) {
          //Translate array expression
          ExpTy arr = transExp(e.array);
          Types.Type actual = arr.ty.actual();
          //Verify that target is array / pointer
          if (actual instanceof Types.ARRAY) {
                  //Verify pointer is an int
                  ExpTy idx = transExp(e.index);
                  checkInt(idx, e.index.pos);
                  Types.ARRAY arrayType = (Types.ARRAY) actual;
                  return new ExpTy(null, arrayType.element);
          }
          error(e.array.pos, "array or pointer type required for subscripting");
          return new ExpTy(null, VOID);
  }
  //AssignExp Translate
  ExpTy transExp(Absyn.AssignExp e) {
          //left hand side
          ExpTy lhs;
          int lhsPos;

          if (e.target != null) {
                  lhs = transExp(e.target);
                  lhsPos = e.target.pos;
                  if (!isLValue(e.targer)) {
                          error(lhsPos, "expression is not assignale (lvalue required)");
                  }
          } else if (e.var != null) {
                  lhs = transVar(e.var);
                  lhsPos = e.var.pos;
          } else {
                  error(e.pos, "missing assignemnt target");
                  return new ExpTy(null, VOID);
          }
          //right hand side
          ExpTy rhs = transExp(e.exp);
          //type rules
          if (e.oper == Absyn.AssignExp.ASSIGN) {
                  //=: numbers -> pointers, distinct int/char
                  if (!isAssignable(lhs.ty, rhs.ty)) {
                          error(e.pos, "incompatible types in assignment");
                  }
          } else {
                  //compound math/bitwise: only accept numbers
                  checkInt(lhs, lhsPos);
                  checkInt(rhs, e.exp.pos);
          }
          return new ExpTy(null, lhs.ty.actual());
  }
  //BreakExp Translate
  ExpTy transExp(Absyn.BreakExp e) {
          if (loopDepth <= 0) {
                  error(e.pos, "break statement not in loop");
          }
          return new ExpTy(null, VOID);
  }
  //CallExp Translate
  ExpTy transExp(Absyn.CallExp e) {
          Types.RECORD formals = null;
          Types.Type resultType = VOID;
          //Direct call to function name
          if (e.func != null) {
                  Entry entry = (Entry) env.venv.get(e.func);
                  if (entry == null) {
                          error(e.pos, "undeclared function: " + e.func);
                          return new ExpTy(null, VOID);
                  }
                  if (!(entry instanceof FunEntry)) {
                          error(e.pos, "'" + e.func + "' is not a function");
                          return new ExpTy(null, VOID);
                  }
                  FunEntry funEntry = (FunEntry) entry;
                  formals = funEntry.formals;
                  resultType = funEntry.result;
          //Indirect call via expression
          } else if (e.callee != null) {
                  ExpTy calleeTy = transExp(e.callee);
                  Types.Type actual = calleeTy.ty.actual();
                  //Unwrap pointer if callee is function pointer
                  if (actual instanceof Types.POINTER) {
                          actual = ((Types.POINTER) actual).element.actual();
                  }
                  if (actual instanceof Types.FUNCTION) {
                          Types.FUNCTION fnType = (Types.FUNCTION) actual;
                          formals = fnType.formals;
                          resultType = fnType.result;
                  } else {
                          error(e.callee.pos, "called object is not a function or function pointer");
                          return new ExpTy(null, VOID);
                  }
          } else {
                  error(e.pos, "missing function target in call expression");
                  return new ExpTy(null, VOID);
          }
          Absyn.ExpList arg = e.args;
          Types.RECORD param = formals;

          while (arg != null && param != null) {
                  ExpTy argTy = transExp(arg.head);
                  if (!isAssignable(param.fieldType, argTy.ty)) {
                          error(arg.head.pos, "argument type mismatch for parameter: " + param.fieldName);
                  }
                  arg = arg.tail;
                  param = param.tail;
          }
          if (arg != null) {
                  error(arg.head.pos, "too many arguments in function call");
          } else if (param != null) {
                  error(e.pos, "too few arguments in function call");
          }
          return new ExpTy(null, resultType.actual());
  }

  //CastExp Translate
  ExpTy transExp(Absyn.CastExp e) {
          //inner expression
          ExpTy inner = transExp(e.expression);
          if (e.targetType == null || e.targetType.trim().isEmpty()) {
                  error(e.pos, "missing target type in cast expression");
                  return new ExpTy(null, VOID);
          }
          //String -> Types.Type
          Types.Type destType = resolveTypeString(e.targetType);
          if (destType == null) {
                  error(e.pos, "unknown type in cast: " + e.targetType);
                  return new ExpTy(null, VOID);
          }
          Types.Type srcActual = inner.ty.actual();
          Types.Type dstActual = destType.actual();
          //Cast Legality
          if (!isCastable(arcActual, dstActual)) {
                  error(e.pos, "invalid type cast to " + e.targetType);
          }
          return new ExpTy(null, dstActual);
  }
  //CharExp Translate
  ExpTy transExp(Absyn.CharExp e) {
          if (e.value == null || e.value.isEmpty()) {
                  error(e.pos, "empty character constant");
          }
          return new ExpTy(null, CHAR);
  }
  //CondExp Translate
  ExpTy transExp(Absyn.CondExp e) {
          //must be int
          ExpTy cond = transExp(e.condition);
          checkInt(cond, e.condition.pos);
          //both branches
          ExpTy thenTy = transExp(e.thenExp);
          ExpTy elseTy = transExp(e.elseExp);
          Types.Type tActual = thenTy.ty.actual();
          Types.Type eActual = elseTy.ty.actual();
          //common result of both branches
          //both branches are leafs
          if (tActual instanceof Types.VOID && eActual instanceof Types.VOID) {
                  return new ExpTy(null, VOID);
          }
          //pointer + int/pointer
          if (isAssignable(tActual, eActual)) {
                  return new ExpTy(null, tActual);
          }
          if (isAssignable(eActual, tActual)) {
                  return new ExpTy(null, eActual);
          }
          //incompatible branch types
          error(e.pos, "incompatible types in conditional expression branches");
          return new ExpTy(null, VOID);
  }
  //EnumMemberExp Translate
  ExpTy transExp(Absyn.EnumMemberExp e) {
          //explicit value is an int
          if (e.value != null) {
                  ExpTy valTy = transExp(e.value);
                  checkInt(valTy, e.value.pos);
          }
          //member is in venv, return its type; else int
          if (e.name != null) {
                  Entry entry = (Entry) env.venv.get(e.name);
                  if (entry instanceof VarEntry) {
                          return new ExpTy(null, ((VarEntry) entry).ty);
                  }
          }
          return new ExpTy(null, INT);
  }
  //FieldExp Translate
  ExpTy transExp(Absyn.FieldExp e) {
          if (e.name == null || e.name.isEmpty()) {
                  error(e.pos, "missing field identifier");
                  return new ExpTy(null, VOID);
          }
          //find id in venv
          Entry entry = (Entry) env.venv.get(Symbol.Symbol.symbol(e.name));
          if (entry instanceof VarEntry) {
                  return new ExpTy(null, ((VarEntry) entry).ty);
          }
          error(e.pos, "undeclared id/invalid standaone field reference: " + e.name);
          return new ExpTy(null, VOID);
  }
  //ForExp Translate
  ExpTy transExp(Absyn.ForExp e) {
          //new scope so loop var is local to for loop
          env.venv.beginScope();
          //translate loop var declaration
          if (e.var != null) {
                  transDec(e.var);
                  //validate that loop var is int
                  if (e.var.entry != null && !INT.coerceTo(e.var.entry.ty)) {
                          error(e.var.pos, "for-loop var must be an int");
                  }
          }
          //validate that upper bound is int
          if (e.hi != null) {
                  ExpTy hiTy = transExp(e.hi);
                  checkInt(hiTy, e.hi.pos);
          }
          //increment loopDepth + type-check loop body
          loopDepth++;
          try {
                  if (e.body != null) {
                          transExp(e.body);
                  }
          } finally {
                  loopDepth--;
          }
          //close loop bar's scope
          env.venv.endScope();
          return new ExpTy(null, VOID);
  }
  //IfExp Translate
  ExpTy transExp(Absyn.IfExp e) {
          //test condition must be int
          ExpTy test = transExp(e.test);
          checkInt(test, e.test.pos);
          //translate then branch
          ExpTy thenTy = transExp(e.thenclause);
          //no else branch
          if (e.elseclause == null) {
                  if (!(thenTy.ty.actual() instanceof Types.VOID)) {
                          error(e.thenclause.pos, "if-then expression without else must produce void");
                  }
                  return new ExpTy(null, VOID);
          }
          //then + else brances exist
          ExpTy elseTy = transExp(e.elseclause);
          Types.Type tActual = thenTy.ty.actual();
          Types.Type eActual = elseTy.ty.actual();
          //no branches
          if (tActual instanceof Types.VOID && eActual instanceof Types.VOID) {
                  return new ExpTy(null, VOID);
          }
          if (isAssignablee(tActual, eActual)) {
                  return new ExpTy(null, tActual);
          }
          if (isAssignable (tActual, eActual)) {
                  return new ExpTy(null, eActual);
          }
          error(e.pos, "incompatible types in if-then-else branches");
          return new ExpTy(null, VOID);
  }
  //IntExp Translate
  ExpTy transExp(Absyn.IntExp e) {
          if (e.lexeme != null) {
                  try {
                          Integer.decode(e.lexeme.trim());
                  } catch (NumberFormatException ex) {
                          error(e.pos, "invalid int literal: " + e.lexeme);
                  }
          }
          return new ExpTy(null, INT);
  }
  //LetExp Translate
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
  //MemberExp Translate
  ExpTy transExp(Absyn.MemberExp e) {
          //translate target expression
          ExpTy targetTy = transExp(e.target);
          Types.Type actual = targetTy.ty.actual();
          //enforce type-checking
          if (e.throughPointer) {
                  if (!(actual instanceof Types.POINTER)) {
                          error(e.target.pos, "pointer type required for '->' member access");
                          return new ExpTy(null, VOID);
                  }//dereference pointer
                  actual = ((Types.POINTER) actual).element.actual();
          } else {
                  if (actual instanceof Types.POINTER) {
                          error(e.target.pos, "member reference type is a pointer");
                          return new ExpTy(null, VOID);
                  }
          }
          //validate that target is struct/union
          if (!(actual instanceof Types.RECORD)) {
                  error(e.target.pos, "request for member '" + e.member + "' in non-struct/union type");
                  return new ExpTy(null, VOID);
          }
          //search struct/union field for e.member
          for (Types.RECORD field = (Types.RECORD) actual; field !- null; field = field.tail) {
                  if (field.fieldName == e.member) {
                          return new ExpTy(null, field.fieldType.actual());
                  }
          }
          error(e.pos, "struct/union has no member named '" + e.member + "'");
          return new ExpTy(null, VOID);
  }
  //NilExp Translate
  ExpTy transExp(Absyn.NilExp e) {
          return new ExpTy(null, NIL);
  }
  //OpExp Translate
  ExpTy transExp(Absyn.OpExp e) {
          ExpTy left = transExp(e.left);
          ExpTy right = transExp(e.right);
          Types.Type lActual = left.ty.actual();
          Types.Type rActual = left.ty.actual();
          switch (e.oper) {
                  //arithmetic, bitwise, shift operators
                  case Absyn.OpExp.PLUS:
                  case Absyn.OpExp.MINUS:
                  case Absyn.OpExp.MUL:
                  case Absyn.OpExp.DIV:
                  case Absyn.OpExp.MOD:
                  case Absyn.OpExp.BIT_AND:
                  case Absyn.OpExp.BIT_OR:
                  case Absyn.OpExp.BIT_XOR:
                  case Absyn.OpExp.SHL:
                  case Absyn.OpExp.SHR:
                          checkInt(left, e.left.pos);
                          checkInt(right, e.right.pos);
                          return new ExpTy(null, INT);
                 //&& and ||
                  case Absyn.OpExp.AND:
                  case Absyn.OpExp.OR:
                          checkInt(left, e.left.pos);
                          checkInt(right, e.right.pos);
                          return new ExpTy(null, INT);
                 //<,<=,>=,>
                  case Absyn.OpExp.LT:
                  case Absyn.OpExp.LE:
                  case Absyn.OpExp.GT:
                  case Absyn.OpExp.GE:
                          if (lActual instanceof Types.INT && rActual instanceof Types.INT) {
                                  return new ExpTy(null, INT);
                          }
                          if (lActual instanceof Types.CHAR && rActual instanceof Types.CHAR) {
                                  return new ExpTy(null, INT);
                          }
                          if (lActual instanceof Types.POINTER && rActual instanceof Types.POINTER) {
                                  return new ExpTy(null, INT);
                          }
                          error(e.pos, "incompatible operand types for relationship operator");
                          return new ExpTy(null, INT);
                //==, !=
                  case Absyn.OpExp.EQ:
                  case Absyn.OpExp.NE:
                          if (isAssignable(lActual, rActual) || isAssignable(rActual, lActual)) {
                                  return new ExpTy(null, INT);
                          }
                          error(e.pos, "incompatible operand types for equality comparison");
                          return new ExpTy(null, INT);
                //comma operator
                  case Absyn.OpExp.COMMA:
                          return new ExpTy(null, rActual);
                default:
                          throw new Error("Semant.transExp(OpExp): unknown operator " + e.oper);
          }
  }
  //ParamExp Translate
  ExpTy transExp(Absyn.ParamExp e) {
          if (e.type == null || e.type.trim().isEmpty()) {
                  error(e.pos, "missing type in param dec");
                  return new ExpTy(null, VOID);
          }
          Types.Type paramType = resolveTypeString(e.type);
          if (paramType == null) {
                  error(e.pos, "unknown param tyep: " + e.type);
                  return new ExpTy(null, VOID);
          }
          return new ExpTy(null, paramType.actual());
  }
  //ParenExp Translate
  ExpTy transExp(Absyn.ParenExp e) {
          if (e.inner == null) {
                  error(e.pos, "empty parenthesized expression");
                  return new ExpTy(null, VOID);
          }
          return transExp(e.inner);
  }
  //RecordExp Translate
  ExpTy transExp(Absyn.RecordExp e) {
          //find record/struct/union in type environment
          Types.Type t = (Types.Type) env.tenv.get(e.typ);
          if (t == null) {
                  error(e.pos, "undefined type: " + e.typ);
                  return new ExpTy(null, VOID);
          }
          //validate that actual is struct/union
          Types.Type actual = t.actual();
          if (!(actual instanceof Types.RECORD)) {
                  error(e.pos, "struct/union type required: " + e.typ);
                  return new ExpTy(null, VOID);
          }
          Types.RECORD recordType = (Types.RECORD) actual;
          //validate init field against record's field def
          java.util.Set<Symbol.Symbol> seenFields = new java.util.HashSet<>();
          for (Absyn.FieldExpList cur = e.fields; cur != null; cur = cur.tail) {
                  if (seenFields.contains(cur.name)) {
                          error(cur.pos, "duplicate field '" + cur.name + "' in init");
                  } else {
                          seenFields.add(cur.name);
                  } //find matching field defs in Types.RECORD
                  Types.RECORD targetField = findRecordField(recordType, cur.name);
                  ExpTy initVal = transExp(cur.init);
                  if (targetField == null) {
                          error(cur.pos, "struct/union '" + e.typ + "' has no member named '" + cur.name + "'");
                  } else if (!isAssignable(targetField.fieldType, initVal.ty)) {
                          error(cur.pos, "incompatible type init field '" + cur.name " "'");
                  }
          }
          return new ExpTy(null, recordType);
  }
  //SeqExp Translate
  ExpTy transExp(Absyn.SeqExp e) {
          ExpTy last = new ExpTy(null, VOID);
          for (Absyn.ExpList cur = e.list; cur != null; cur = cur.tail) {
                  if (cur.head != null) {
                          last = transExp(cur.head);
                  }
          }
          return new ExpTy(null, last.ty.actual());
  }
  //SizeofExp Translate
  ExpTy transExp(Absyn.SizeofExp e) {
          Types.Type targetType = null;
          //sizeof(expression)
          if (e.expression != null) {
                  ExpTy exprTy = transExp(e.expression);
                  targetType = exprTy.ty.actual();
          } else if (e.type != null && !e.type.trim().isEmpty()) {//sizeof(type)
                  Types.Type resolved = resolveTypeString(e.type);
                  if (resolved == null) {
                          error(e.pos, "unknows type in sizeof: " + e.type);
                          return new ExpTy(null, INT);
                  }
                  targetType = resolved.actual();
          } else {
                  error(e.pos, "missing operand in sizeof expression");
                  return new ExpTy(null, INT);
          }//reject sizeof(void)
          if (targetType instanceof Types.VOID) {
                  error(e.pos, "invalid application of sizeof to a void type");
          }
          return new ExpTy(null, INT);
  }
  //StringExp Translate
  ExpTy transExp(Absyn.StringExp e) {
          if (e.value == null) {
                  error(e.pos, "invalid string literal");
          }
          return new ExpTy(null, STRING);
  }
  //UnaryOpExp Translate
  ExpTy transExp(Absyn.UnaryOpExp e) {
          ExpTy operand = transExp(e.exp);
          Types.Type actual = operand.ty.actual();
          switch (e.oper) {
                  //unary arithmetic + bitwise operators; only ints
                  case Absyn.UnaryOpExp.POS:
                  case Absyn.UnaryOpExp.NEG:
                  case Absyn.UnaryOpExp.BIT_NOT:
                          checkInt(operand, e.exp.pos);
                          return new ExpTy(null, INT);
                  //!x; accepts ints + produces 0/1
                  case Absyn.UnaryOpExp.NOT:
                          checkInt(operand, e.exp.pos);
                          return new ExpTy(null, INT);
                  //increment + decrement operators: ++x, --x, x++, x--
                  case Absyn.UnaryOpExp.PRE_INC:
                  case Absyn.UnaryOpExp.PRE_DEC:
                  case Absyn.UnaryOpExp.POST_INC:
                  case Absyn.UnaryOpExp.POST_DEC:
                          if (!isLValue(e.exp)) {
                                  error(e.exp.pos, "lvalue required as incrment/decrement operand");
                          }
                          checkInt(operand, e.exp.pos);
                          return new ExpTy(nul, INT);
                  //address of (&x); requires address + returns pointer
                  case Absyn.UnaryOpExp.ADDR_OF:
                          if (!isLValue(e.exp)) {
                                  error(e.exp.pos, "lvalue required as unary & operand");
                                  return new ExpTy(null, VOID);
                          }
                          return new ExpTY(null, new Types.POINTER(actual));
                  //pointer dereference (*ptr); requires pointer + returns pointed-to type
                  case Absyn.UnaryOpExp.DEREF:
                          if (!(actual instanceof Types.POINTER)) {
                                  error(e.exp.pos, "invalid type arg of unary * (have '" + actual + "')");
                                  return new ExpTy(null, VOID);
                          }
                          Types.POINTER ptrType = (Types.POINTER) actual;
                          return new ExpTy(null, ptrType.element.actual());
                  default:
                          throw new Error("Semant.transExp(UnaryOpExp): unknown operator " + e.oper);
          }
  }
  //VarExp Translate
  ExpTy transExp(Absyn.VarExp e) {
          if (e.var == null) {
                  error(e.pos, "missing var in var expression");
                  return new ExpTy(null, VOID);
          }
          return transVar(e.var);
  }
  //WhileExp Translate
  ExpTy transExp(Absyn.WhileExp e) {
          //must be int
          if (e.test != null) {
                  ExpTy testTy = transExp(e.test);
                  checkInt(testTy, e.test.pos);
          } else {
                  error(e.pos, "missing condition in while expression");
          }
          //increment loopDepth so BreakExp in body is valid
          loopDepth++;
          try {
                  if (e.body != null) {
                          ExpTy bodyTy = transExp(e.body);
                          if (!(bodyTy.ty.actual() instanceof Types.VOID)) {
                                  error(e.body.pos, "While loop body must produce void");
                          }
                  }
          } finally {
                  loopDepth--;
          }
          return new ExpTy(null, VOID);
  }

  //Declaration Dispatch
  ExpTy transDec(Absyn.Dec e) {
          ExpTy result;

          if (e instanceof Absyn.VarDec)
                  return transDec((Absyn.VarDec)d);
          if (e instanceof Absyn.FunctionDec)
                  return transDec((Absyn.FunctionDec)d);
          if (e instanceof Absyn.FunPrototypeDecl)
                  return transDec((Absyn.FunPrototypeDecl)d);
          if (e instanceof Absyn.TypeDec)
                  return transDec((Absyn.TypeDec)d);
          if (e instanceof Absyn.TypedefDecl)
                  return transDec((Absyn.TypedefDecl)d);
          if (e instanceof Absyn.StructUnionDecl)
                  return transDec((Absyn.StructUnionDecl)d);
          if (e instanceof Absyn.EnumDecl)
                  return transDec((Absyn.EnumDecl)d);
          else throw new Error("Semant.transDec");
          e.type = result.ty;
          return result;
  }

  //VarDec Translate
  Exp transDec(Absyn.VarDec d) {
          Type type;
          if (d.typ == null) {
                  //untyped declaration
                  if (d.typ == null) {
                          error(d.pos, "untyped variable '" + d.name + "' requires an init");
                          type = VOID;
                  } else if (d.init instanceof Absyn.ArrayInitExp) {
                          error(d.init.pos, "cannot infer type from init list");
                          type = VOID;
                  } else {
                          ExpTy init = transExp(d.init);
                          if (init.ty.actual() instanceof Types.NIL) {
                                  error(d.init.pos, "cannot init untyped var to nil");
                          }
                          type = init.ty.actual();
                  }
          } else {
                  //explicitly typed declaration
                  type = transTy(d.typ);
                  if (type == null) {
                          error(d.typ.pos, "undefined typed: " + d.typ.name);
                          type = VOID;
                  }
                  Type actualTarget = type.actual();
                  if (d.init != null) {
                          if (actualTarget instanceof Types.ARRAY) {
                                  //arr is init w/list
                                  if (!(d.init instanceof Absyn.ArrayInitExp)) {
                                          error(d.init.pos, "array must be init w/list");
                                  } else {
                                          checkInitList((Absyn.ArrayInitExp) d.init, actualTarget);
                                  }
                          } else if (actualTarget instanceof Types.RECORD) {
                                  //structs is init w/list; unions match any member
                                  if (d.init instanceof Absyn.ArrayInitExp) {
                                          checkInitList((Absyn.ArrayInitExp) d.init, actualTarget) ;
                                  } else if (unionTypes.contains(actualTarget)) {
                                          ExpTy init = transExp(d.init);
                                          boolean matchesMember = false;
                                          for (Types.RECORD m = (Types.RECORD) actualTarget; m != null; m = m.tail) {
                                                  if (isAssignable(m.fieldType, init.ty)) {
                                                          matchesMember = true;
                                                          break;
                                                  }
                                          }
                                          if (!matchesMember) {
                                                  error(d.init.pos, "union inti doesn't match any member type");
                                          }
                                  } else {
                                          ExpTy init = transExp(d.init);
                                          if (!isAssignable(actualTarget, init.ty)) {
                                                  error(d.init.pos, "type mismatch in struct init");
                                          }
                                  }
                          } else {//INT, CHAR, POINTER
                                if (d.init instanceof Absyn.ArrayInitExp) {
                                        error(d.init.pos, "scalar/pointer cannot be init w/list");
                                } else {
                                        ExpTy init = transExp(d.init);
                                        if (!isAssignable(actualTarget, init.ty)) {
                                                error(d.init.pos, "type mismatch in var init");
                                        }
                                }
                          }
                  }
          }
          d.entry = new VarEntry(type);
          env.venv.put(d.name, d.entry);
          return null;
  }
  //FunctionDec Translate
  Exp transDec(Absyn.FunctionDec d) {
          //register all function headers into mutually recursive block
          java.util.Set<Symbol.Symbol> seenNames = new java.util.HashSet<>();
          for (Absyn.FunctionDec cur = d; cur != null; cur = cur.next) {
                  if (seenNames.contains(cur.name)) {
                          error(cur.pos, "dupe funct def in mutually recursive block: " + cur.name);
                  } else {
                          seenNames.add(cur.name);
                  }
                  //resolve optional return type
                  Types.Type resultType = VOID;
                  if (cur.result != null) {
                          resultType = transTy(cur.result);
                          if (resultType == null) {
                                  error(cur.result.pos, "unkown return type: " + cur.result.name);
                                  resultType = VOID;
                          }
                  }
                  Types.RECORD formals = transFieldList(cur.params);
                  env.venv.put(cur.name, new FunEntry(formals, resultType));
          }//type-check func body in its own param scope
          for (Absyn.FunctionDec cur = d; cur != null; cur = cur.next) {
                  FunEntry funEntry = (FunEntry) env.venv.get(cur.name);
                  env.venv.beginScope();
                  env.tenv.beginScope();
                  //bind formals into venv
                  for (Types.RECORD param = funEntry.formals; param != null; param = param.tail) {
                          env.venv.put(param.fieldName, new VarEntry(param.fieldType));
                  }
                  Types.Type prevReturnType = currentReturnType;
                  currentReturnType = funEntry.result.actual();
                  try {
                          if (cur.body != null) {
                                  ExpTy bodyTy = transExp(cur.body);
                                  if (!(funEntry.result.actual() instanceof Types.VOID)) {
                                          if (!isAssignable(funEntry.result, bodyTy.ty)) {
                                                  error(cur.body.pos, "func body type doesn't match declared return type");
                                          }
                                  } else if (!(bodyTy.ty.actual() instanceof Types.VOID)) {
                                          error(cur.body.pos, "void function cannot return a value");
                                  }
                          }
                  } finally {
                          currentReturnType = prevReturnType;
                  }
                  env.venv.endScope();
                  env.tenv.endScope();
          }
          return null;
  }
  //FunPrototypeDecl Translate
  Exp transDec(Absyn.FunPrototypeDecl d) {
          //resolve return type string
          Types.Type returnType = (d.type != null) ? resolveTypeString(d.type) : VOID;
          if (returnType == null) {
                  error(d.pos, "unknown return type in prototype: " + d.type);
                  returnType = VOID;
          }//build formal parameter list
          Types.RECORD formals = buildParamRecord(d.parameters);
          //check for decs in current scope
          Entry existing = (Entry) env.venv.get(d.name);
          if (existing != null) {
                  if (!(existing instanceof FunEntry)) {
                          error(d.pos, "'" + d.name + "' redeclared as a diff kind of symbol");
                          return null;
                  }
                  //if(prototype/def already exists), then verify signatures match
                  FunEntry prev = (FunEntry) existing;
                  if (!sameSignature(prev.formals, prev.result, formals, returnType)) {
                          error(d.pos, "conflicting types for function '" + d.name + "'");
                  }
                  return null;
          }//register func signature in venv
          env.venv.put(d.name, new FunEntry(formals, returnType));
          return null;
  }
  //TypeDec Translate
  Exp transDec(Absyn.TypeDec d) {
          //register all type headers in tenv
          java.util.Set<Symbol.Symbol> seenNames = new java.util.HashSet<>();
          for(Absyn.TypeDec cur = d; cur != null; cur = cur.next) {
                  if (seenNames.contains(cur.name)) {
                          error(cur.pos, "dupe type def in mutually recursive block: " + cur.name);
                  } else {
                          seenNames.add(cur.name);
                  }
                  env.tenv.put(cur.name, new Types.NAME(cur.name));
          }//translate each type body + bind to Types.NAME header
          for (Absyn.TypeDec cur = d; cur != null; cur = cur.next) {
                  Types.NAME nameType = (Types.NAME) env.tenv.get(cur.name);
                  if (nameType != null) {
                          nameType.bing(transTy(cur.ty));
                  }
          }//detect cyclic type defs (type a = b; type b = a;)
          for (Absyn.TypeDec cur = d; cur != null; cur = cur.next) {
                  Types.NAME nameType = (Types.NAME) env.tenv.get(cur.name);
                  if (nameType != null && nameType.isLoop()) {
                          error(cur.pos, "illegal cyclic type def: " + cur.name);
                          nameType.bind(VOID);
                  }
          }
          return null;
  }
  //TypedefDecl Translate
  Exp transDec(Absyn.TypedefDecl d) {
          if (d.alias == null) {
                  error(d.pos, "missing alias name in typedef dec");
                  return null;
          }//resolve target type string
          Types.Type targetType = (d.type != null) ? resolveTypeString(d.type) : null;
          if (targetType == null) {
                  error(d.pos, "unknown type in typedef: " + d.type);
                  targetType = VOID;
          }//check if alias name is alread defined in current type scope
          Types.Type existing = (Types.Type) env.tenv.get(d.alias);
          if (existing != null) {
                  Types.Type prevActual = existing.actual();
                  Types.Type newActual = targetType.actual();
                  if (!prevActual.coerceTo(newActual) || !newActual.coerceTo(prevActual)) {
                          error(d.pos, "typedef '" + d.alias + "' redeclared with a diff type");
                  }
                  return null;
          }//wrap in Types.NAME + bind to the resovled type in env.tenv
          Types.NAME nameType = new Types.NAME(d.alias);
          nameType.bind(targetType);
          env.tent.put(d.alias, nameType);
          return null;
  }

  //StructUnionDecl Translate
  Exp transDec(Absyn.StructUnionDecl d) {
          boolean isUnion = "union".equals(d.kind);
          //self-referential pointers
          Type.NAME nameType = null;
          if (d.name != null) {
                  if (env.tenv.get(d.name) != null) {
                          error(d.pos, "redeclaration of " + d.kind + " '" + d.name + "'");
                  }
                  nameType = new Types.NAME(d.name);
                  env.tenv.put(d.name, nameType);
          }//build field chain from d.fields
          Types.RECORD head = null;
          Types.RECORD tail = null;
          java.util.Set<Symbol.Symbol> seenFields = new java.util.HashSet<>();
          for (Absyn.DecList cur = d.fields; cur != null; cur = cur.tail) {
                  if (cur.head instanceof Absyn.VarDecl) {
                          Absyn.VarDecl fieldDecl = (Absyn.VarDecl) cur.head;
                          if (seenFields.contains(fieldDecl.name)) {
                                  error(fieldDecl.pos, "dupe member '" + fieldDecl.name + "' in " + d.kind);
                          } else {
                                  seenFields.add(fieldDecl.name);
                          }//resolve the field's declared type
                          Types.Type fieldType = resolveFieldType(fieldDecl);
                          //send error for self-containment structs (struct A { struct A x; };)
                          if (fieldType == nameType) {
                                  error(fieldDecl.pos, "field '" + fieldDecl.name + "' has incomplete type");
                                  fieldType = VOID;
                          }
                          Types.RECORD node = new Types.RECORD(fieldDecl.name, fieldType, null);
                          if (head == null) {
                                  head = node;
                                  tail = node;
                          } else {
                                  tail.tail = node;
                                  tail = node;
                          }
                  } else if (cur.head != null) {
                          error(cur.head.pos, "invalid member dec insdie " + d.kind);
                  }
          }//inits will enforce union rules
          if (isUnion && head != null) {
                  unionTypes.add(head);
          }//bind RECORD type to placeholder NAME
          if (nameType != null) {
                  nameType.bind(head);
          }
          return null;
  }
  //EnumDecl Translate
  Exp transDec(Absyn.EnumDecl d) {
          //register enum type in tenv if tag name is present
          if (d.name != null) {
                  if (env.tenv.get(d.name) != null) {
                          error(d.pos, "redec of enum type: " + d.name);
                  }//maps enum type to INT
                  env.tenv.put(d.name, INT);
          }//register each enum constant in venv as int var
          for (Absyn.ExpList cur = d.members; cur != null; cur = cur.tail) {
                  Absyn.Exp member = cur.head;
                  if (member instanceof Absyn.EnumMemberExp) {
                          Absyn.EnumMemberExp em = (Absyn.EnumMemberExp) member;
                          //if member w/explicit value, verify it is int
                          if (em.value != null) {
                                  ExpTy valTy = transExp(em.value);
                                  checkInt(valTy, em.value.pos);
                          }
                          if (env.venv.get(em.name) != null) {
                                  error(em.pos, "redec of enum constant: " + em.name);
                          }//register the constant in venv as INT
                          env.venv.put(em.name, new VarEntry(INT));
                          em.type = INT;
                  } else if (member != null) {
                          error(member.pos, "invalid member in enum dec");
                  }
          }
          return null;
  }

  //Statement Dispatch
  ExpTy transStm(Absyn.Stm e) {
          ExpTy result;

          if (e instanceof Absyn.BreakStm)
                  return transStm((Absyn.BreakStm)s);
          if (e instanceof Absyn.CompoundStm)
                  return transStm((Absyn.CompoundStm)s);
          if (e instanceof Absyn.ContinueStm)
                  return transStm((Absyn.ContinueStm)s);
          if (e instanceof Absyn.DoWhileStm)
                  return transStm((Absyn.DoWhileStm)s);
          if (e instanceof Absyn.ExpStm)
                  return transStm((Absyn.ExpStm)s);
          if (e instanceof Absyn.IfStm)
                  return transStm((Absyn.IfStm)s);
          if (e instanceof Absyn.ReturnStm)
                  return transStm((Absyn.ReturnStm)s);
          else throw new Error("Semant.transStm");
          e.type = result.ty;
          return result;
  }

  //BreakStm Translate
  ExpTy transStm(Absyn.BreakStm e) {
          if (loopDepth <= 0) {
                  error(e.pos, "break stm not within a loop");
          }
          return new ExpTy(null, VOID);
  }
  //CompoundStm Translate
  ExpTy transStm(Absyn.CompoundStm e) {
          env.venv.beginScope();
          env.tenv.beginScope();
          //local declarations
          for (Absyn.DecList d = e.declarations; d != null; d = d.tail) {
                  transDec(d.head);
          }
          //all stm in block
          ExpTy lastStm = new ExpTy(null, VOID);
          for (Absyn.StmList s = e.statements; s != null; s = s.tail) {
                  lastStm = transStm(s.head);
          }
          env.venv.endScope();
          env.tenv.endScope();
          return new ExpTy(null, VOID);
  }
  //ContinueStm Translate
  ExpTy transStm(Absyn.ContinueStm e) {
          if (loopDepth <= 0) {
                  error(e.pos, "cont stm not within a loop");
          }
          return new ExpTy(null, VOID);
  }
  //DoWhileStm Translate
  ExpTy transStm(Absyn.DoWhileStm e) {
          //increment loopDepth to preserve break/continue inbody
          loopDepth++;
          try {
                  transStm(e.body);
          } finally {
                  loopDepth--;
          }//validate that loop cond evals to int
          if (e.condition != null) {
                  ExpTy cond = transExp(e.condition);
                  checkInt(cond, e.condition.pos);
          } else {
                  error(e.pos, "missing condition in do-while stm")'
          }
          return new ExpTy(null, VOID);
  }
  //ExpStm Translate
  ExpTy transStm(Absyn.ExpStm e) {
          if (e.exp != null) {
                  transExp(e.exp);
          }
          return new ExpTy(null, VOID);
  }
  //IfStm Translate
  ExpTy transStm(Absyn.IfStm e) {
          //cond must eval to int
          if (e.condition != null) {
                  ExpTy cond = transExp(e.condition);
                  checkInt(cond, e.condition.pos);
          } else {
                  error(e.pos, "missing cond in if stm");
          }//translate then branch
          if (E.thenStm != null) {
                  transStm(e.thenStm);
          }//translate else branch
          if (e.elseStm != null) {
                  transStm(e.elseStm);
          }
          return new ExpTy(null, VOID);
  }
  //ReturnStm Translate
  ExpTy transStm(Absyn.ReturnStm e) {
          //ensure return appears in func
          if (currentReturnType == null) {
                  error(e.pos, "return stm outside of func");
                  if (e.value != null) {
                          transExp(e.value);
                  }
                  return new ExpTy(null, VOID);
          }
          Types.Type expected = currentReturnType.actual();
          //return
          if (e.value == null) {
                  if (!(expected instanceof Types.VOID)) {
                          error(e.pos, "non-void func must return a value");
                  }
                  return new ExpTy(null, VOID);
          }//return expr
          ExpTy valTy = transExp(e.value);
          if (expected instanceof Types.VOID) {
                  error(e.value.pos, "void func cannot return a value");
          } else if (!isAssignable(expected, valTy.ty)) {
                  error(e.value.pos, "incompatible return type");
          }
          return new ExpTy(null, VOID);
  }
}
