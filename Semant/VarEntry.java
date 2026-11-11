package Semant;

public class VarEntry extends Entry {
  public Types.Type ty;
  public Frame.Access access;
  VarEntry(Types.Type t, Frame.Access a) {
    ty = t;
    access = a;
  }
}
