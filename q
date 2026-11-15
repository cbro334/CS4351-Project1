javac -g -cp ".:/classes/cs4351/cs4351_bau/pub/classes:/classes/cs4351/cs4351_bau/pub/classes/chap4" Absyn/*.java Parse/*.java
./Symbol/Symbol.java:1: error: illegal character: '\ufeff'
﻿package Symbol;
^
./Symbol/Symbol.java:1: error: class, interface, enum, or record expected
﻿package Symbol;
         ^
./Symbol/Symbol.java:2: error: class, interface, enum, or record expected
import java.util.HashMap;
^
Absyn/Absyn.java:2: error: cannot access Symbol
import Symbol.Symbol;
             ^
  bad source file: ./Symbol/Symbol.java
    file does not contain class Symbol.Symbol
    Please remove or make sure it appears in the correct subdirectory of the sourcepath.
Absyn/ArrayExp.java:6: error: cannot find symbol
    public Symbol typ;
           ^
  symbol:   class Symbol
  location: class ArrayExp
Absyn/ArrayExp.java:9: error: cannot find symbol
    public ArrayExp(int p, Symbol t, Exp s, Exp i) {
                           ^
  symbol:   class Symbol
  location: class ArrayExp
Absyn/ArrayTy.java:4: error: cannot find symbol
   public Symbol typ;
          ^
  symbol:   class Symbol
  location: class ArrayTy
Absyn/ArrayTy.java:5: error: cannot find symbol
   public ArrayTy(int p, Symbol t) {pos=p; typ=t;}
                         ^
  symbol:   class Symbol
  location: class ArrayTy
Absyn/CallExp.java:6: error: cannot find symbol
    public Symbol func;
           ^
  symbol:   class Symbol
  location: class CallExp
Absyn/CallExp.java:10: error: cannot find symbol
    public CallExp(int p, Symbol f, ExpList a) {
                          ^
  symbol:   class Symbol
  location: class CallExp
Absyn/EnumDecl.java:5: error: cannot find symbol
    public Symbol name;
           ^
  symbol:   class Symbol
  location: class EnumDecl
Absyn/EnumDecl.java:8: error: cannot find symbol
    public EnumDecl(int p, Symbol n, ExpList m) {
                           ^
  symbol:   class Symbol
  location: class EnumDecl
Absyn/EnumMemberExp.java:5: error: cannot find symbol
    public Symbol name;
           ^
  symbol:   class Symbol
  location: class EnumMemberExp
Absyn/EnumMemberExp.java:8: error: cannot find symbol
    public EnumMemberExp(int p, Symbol n, Exp v) {
                                ^
  symbol:   class Symbol
  location: class EnumMemberExp
Absyn/FieldExpList.java:6: error: cannot find symbol
    public Symbol name;
           ^
  symbol:   class Symbol
  location: class FieldExpList
Absyn/FieldExpList.java:10: error: cannot find symbol
    public FieldExpList(int p, Symbol n, Exp i, FieldExpList t) {
                               ^
  symbol:   class Symbol
  location: class FieldExpList
Absyn/FieldList.java:6: error: cannot find symbol
   public Symbol name;
          ^
  symbol:   class Symbol
  location: class FieldList
Absyn/FieldList.java:7: error: cannot find symbol
   public Symbol typ;
          ^
  symbol:   class Symbol
  location: class FieldList
Absyn/FieldList.java:11: error: cannot find symbol
   public FieldList(int p, Symbol n, Symbol t, FieldList x) {pos = p; name = n; typ = t; tail = x;}
                           ^
  symbol:   class Symbol
  location: class FieldList
Absyn/FieldList.java:11: error: cannot find symbol
   public FieldList(int p, Symbol n, Symbol t, FieldList x) {pos = p; name = n; typ = t; tail = x;}
                                     ^
  symbol:   class Symbol
  location: class FieldList
Absyn/FieldVar.java:7: error: cannot find symbol
   public Symbol field;
          ^
  symbol:   class Symbol
  location: class FieldVar
Absyn/FieldVar.java:9: error: cannot find symbol
   public FieldVar(int p, Var v, Symbol f) {pos=p; var=v; field=f;}
                                 ^
  symbol:   class Symbol
  location: class FieldVar
Absyn/VarDec.java:4: error: cannot find symbol
   public Symbol name;
          ^
  symbol:   class Symbol
  location: class VarDec
Absyn/VarDec.java:8: error: cannot find symbol
   public VarDec(int p, Symbol n, NameTy t, Exp i) {pos=p; name=n; typ=t; init=i;}
                        ^
  symbol:   class Symbol
  location: class VarDec
Absyn/NameTy.java:4: error: cannot find symbol
   public Symbol name;
          ^
  symbol:   class Symbol
  location: class NameTy
Absyn/NameTy.java:5: error: cannot find symbol
   public NameTy(int p, Symbol n) {pos=p; name=n;}
                        ^
  symbol:   class Symbol
  location: class NameTy
Absyn/FunDecl.java:7: error: cannot find symbol
    public Symbol name;
           ^
  symbol:   class Symbol
  location: class FunDecl
Absyn/FunDecl.java:12: error: cannot find symbol
                   Symbol name, ExpList parameters, Stm body) {
                   ^
  symbol:   class Symbol
  location: class FunDecl
Absyn/FunPrototypeDecl.java:7: error: cannot find symbol
    public Symbol name;
           ^
  symbol:   class Symbol
  location: class FunPrototypeDecl
Absyn/FunPrototypeDecl.java:10: error: cannot find symbol
    public FunPrototypeDecl(int p, ExpList mods, String t, Symbol n, ExpList params) {
                                                           ^
  symbol:   class Symbol
  location: class FunPrototypeDecl
Absyn/FunctionDec.java:6: error: cannot find symbol
  public Symbol name;
         ^
  symbol:   class Symbol
  location: class FunctionDec
Absyn/FunctionDec.java:12: error: cannot find symbol
  public FunctionDec(int p, Symbol n, FieldList a, NameTy r, Exp b,
                            ^
  symbol:   class Symbol
  location: class FunctionDec
Absyn/MemberExp.java:6: error: cannot find symbol
    public Symbol member;
           ^
  symbol:   class Symbol
  location: class MemberExp
Absyn/MemberExp.java:9: error: cannot find symbol
    public MemberExp(int p, Exp e, Symbol m, boolean ptr) {
                                   ^
  symbol:   class Symbol
  location: class MemberExp
Absyn/TypeDec.java:4: error: cannot find symbol
   public Symbol name;
          ^
  symbol:   class Symbol
  location: class TypeDec
Absyn/TypeDec.java:7: error: cannot find symbol
   public TypeDec(int p, Symbol n, Ty t, TypeDec x) {pos=p; name=n; ty=t; next=x;}
                         ^
  symbol:   class Symbol
  location: class TypeDec
Absyn/RecordExp.java:6: error: cannot find symbol
    public Symbol typ;
           ^
  symbol:   class Symbol
  location: class RecordExp
Absyn/RecordExp.java:9: error: cannot find symbol
    public RecordExp(int p, Symbol t, FieldExpList f) {
                            ^
  symbol:   class Symbol
  location: class RecordExp
Absyn/SimpleVar.java:6: error: cannot find symbol
   public Symbol name;
          ^
  symbol:   class Symbol
  location: class SimpleVar
Absyn/SimpleVar.java:8: error: cannot find symbol
   public SimpleVar (int p, Symbol n) {pos=p; name=n;}
                            ^
  symbol:   class Symbol
  location: class SimpleVar
Absyn/StructUnionDecl.java:7: error: cannot find symbol
    public Symbol name;
           ^
  symbol:   class Symbol
  location: class StructUnionDecl
Absyn/StructUnionDecl.java:10: error: cannot find symbol
    public StructUnionDecl(int p, ExpList m, String k, Symbol n, DecList f) {
                                                       ^
  symbol:   class Symbol
  location: class StructUnionDecl
Absyn/TypedefDecl.java:6: error: cannot find symbol
    public Symbol alias;
           ^
  symbol:   class Symbol
  location: class TypedefDecl
Absyn/TypedefDecl.java:8: error: cannot find symbol
    public TypedefDecl(int p, String t, Symbol n) {
                                        ^
  symbol:   class Symbol
  location: class TypedefDecl
Absyn/VarDecl.java:7: error: cannot find symbol
    public Symbol name;
           ^
  symbol:   class Symbol
  location: class VarDecl
Absyn/VarDecl.java:12: error: cannot find symbol
                   Symbol name, Exp initializer) {
                   ^
  symbol:   class Symbol
  location: class VarDecl
Parse/Grm.java:757: error: cannot find symbol
 static Symbol sym(String s) {
        ^
  symbol:   class Symbol
  location: class CUP$Grm$actions
./Symbol/Symbol.java:7: error: cannot find symbol
    private static HashMap<String, Symbol> dict = new HashMap<>();
                   ^
  symbol:   class HashMap
  location: class Symbol
Parse