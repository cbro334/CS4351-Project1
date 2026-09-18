package Parse;
import ErrorMsg.ErrorMsg;

%%

%implements Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char
%full

%state COMMENT, STRING, CHARACTER

%{
private ErrorMsg errorMsg;
private StringBuilder sb = new StringBuilder();
private int start = -1;
private int commentStart = -1;
private boolean invalid = false;

private void newline() {
  errorMsg.newline(yychar + yylength() - 1);
}

private void err(int pos, String s) {
  errorMsg.error(pos,s);
}

private void err(String s) {
  err(yychar,s);
}

private java_cup.runtime.Symbol tok(int kind) {
  return tok(kind, null);
}

private java_cup.runtime.Symbol tok(int kind, Object value) {
  return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}

private java_cup.runtime.Symbol literal(int kind) {
  return new java_cup.runtime.Symbol(kind, start, yychar+yylength(), sb.toString());
}

private void startLiteral() {
  start = yychar;
  sb.setLength(0);
  invalid = false;
}

private void escape(String s) {
  char c = s.charAt(1);

  if (c == 'x') {
    int value = 0;
    for (int i = 2; i < s.length(); i++) {
      value = (value * 16 + Character.digit(s.charAt(i), 16)) & 255;
    }
    sb.append((char)value);
    return;
  }

  if (c >= '0' && c <= '7') {
    int value = 0;
    for (int i = 1; i < s.length(); i++) {
      value = value * 8 + (s.charAt(i) - '0');
    }
    sb.append((char)(value & 255));
    return;
  }

  switch (c) {
    case 'a': sb.append('\007'); break;
    case 'b': sb.append('\b'); break;
    case 'f': sb.append('\f'); break;
    case 'n': sb.append('\n'); break;
    case 'r': sb.append('\r'); break;
    case 't': sb.append('\t'); break;
    case 'v': sb.append('\013'); break;
    case '\\': sb.append('\\'); break;
    case '\'': sb.append('\''); break;
    case '"': sb.append('"'); break;
    case '?': sb.append('?'); break;
    default:
      err("Invalid escape sequence: " + s);
      invalid = true;
  }
}

Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}

%}

%eofval{
  if (commentStart >= 0) {
    err(commentStart, "Unterminated comment");
    commentStart = -1;
  }
  if (start >= 0) {
    err(start, "Unterminated literal");
    start = -1;
  }
  return tok(sym.EOF, null);
%eofval}

SUFFIX = ([uU][lL]?|[lL][uU]?)?

%%

<YYINITIAL> "auto"     {return tok(sym.AUTO, null);}
<YYINITIAL> "break"    {return tok(sym.BREAK, null);}
<YYINITIAL> "case"     {return tok(sym.CASE, null);}
<YYINITIAL> "char"     {return tok(sym.CHAR, null);}
<YYINITIAL> "const"    {return tok(sym.CONST, null);}
<YYINITIAL> "continue" {return tok(sym.CONTINUE, null);}
<YYINITIAL> "default"  {return tok(sym.DEFAULT, null);}
<YYINITIAL> "do"       {return tok(sym.DO, null);}
<YYINITIAL> "double"   {return tok(sym.DOUBLE, null);}
<YYINITIAL> "else"     {return tok(sym.ELSE, null);}
<YYINITIAL> "enum"     {return tok(sym.ENUM, null);}
<YYINITIAL> "extern"   {return tok(sym.EXTERN, null);}
<YYINITIAL> "float"    {return tok(sym.FLOAT, null);}
<YYINITIAL> "for"      {return tok(sym.FOR, null);}
<YYINITIAL> "goto"     {return tok(sym.GOTO, null);}
<YYINITIAL> "if"       {return tok(sym.IF, null);}
<YYINITIAL> "int"      {return tok(sym.INT, null);}
<YYINITIAL> "long"     {return tok(sym.LONG, null);}
<YYINITIAL> "register" {return tok(sym.REGISTER, null);}
<YYINITIAL> "return"   {return tok(sym.RETURN, null);}
<YYINITIAL> "short"    {return tok(sym.SHORT, null);}
<YYINITIAL> "signed"   {return tok(sym.SIGNED, null);}
<YYINITIAL> "sizeof"   {return tok(sym.SIZEOF, null);}
<YYINITIAL> "static"   {return tok(sym.STATIC, null);}
<YYINITIAL> "struct"   {return tok(sym.STRUCT, null);}
<YYINITIAL> "switch"   {return tok(sym.SWITCH, null);}
<YYINITIAL> "typedef"  {return tok(sym.TYPEDEF, null);}
<YYINITIAL> "union"    {return tok(sym.UNION, null);}
<YYINITIAL> "unsigned" {return tok(sym.UNSIGNED, null);}
<YYINITIAL> "void"     {return tok(sym.VOID, null);}
<YYINITIAL> "volatile" {return tok(sym.VOLATILE, null);}
<YYINITIAL> "while"    {return tok(sym.WHILE, null);}
<YYINITIAL> "var"      {return tok(sym.VAR, null);}
<YYINITIAL> "fun"      {return tok(sym.FUN, null);}

<YYINITIAL> [A-Za-z_][A-Za-z0-9_]* {return tok(sym.ID, yytext());}

<YYINITIAL> 0[xX][0-9a-fA-F]+{SUFFIX} {return tok(sym.DECIMAL_LITERAL, yytext());}
<YYINITIAL> 0[0-7]*{SUFFIX} {return tok(sym.DECIMAL_LITERAL, yytext());}
<YYINITIAL> [1-9][0-9]*{SUFFIX} {return tok(sym.DECIMAL_LITERAL, yytext());}
<YYINITIAL> [0-9][A-Za-z0-9_]* {err("Invalid integer literal: " + yytext());}

<YYINITIAL> ">>=" {return tok(sym.RSHIFTASSIGN, null);}
<YYINITIAL> "<<=" {return tok(sym.LSHIFTASSIGN, null);}
<YYINITIAL> "..." {return tok(sym.ELIPSES, null);}
<YYINITIAL> "++"  {return tok(sym.INCREMENT, null);}
<YYINITIAL> "--"  {return tok(sym.DECREMENT, null);}
<YYINITIAL> "->"  {return tok(sym.ARROW, null);}
<YYINITIAL> "&&"  {return tok(sym.AND, null);}
<YYINITIAL> "||"  {return tok(sym.OR, null);}
<YYINITIAL> "=="  {return tok(sym.EQ, null);}
<YYINITIAL> "!="  {return tok(sym.NEQ, null);}
<YYINITIAL> "<="  {return tok(sym.LE, null);}
<YYINITIAL> ">="  {return tok(sym.GE, null);}
<YYINITIAL> "<<"  {return tok(sym.LSHIFT, null);}
<YYINITIAL> ">>"  {return tok(sym.RSHIFT, null);}
<YYINITIAL> "+="  {return tok(sym.ADDASSIGN, null);}
<YYINITIAL> "-="  {return tok(sym.SUBASSIGN, null);}
<YYINITIAL> "*="  {return tok(sym.MULASSIGN, null);}
<YYINITIAL> "/="  {return tok(sym.DIVASSIGN, null);}
<YYINITIAL> "%="  {return tok(sym.MODASSIGN, null);}
<YYINITIAL> "&="  {return tok(sym.BWISEANDASSIGN, null);}
<YYINITIAL> "|="  {return tok(sym.BWISEORASSIGN, null);}
<YYINITIAL> "^="  {return tok(sym.BWISEXORASSIGN, null);}
<YYINITIAL> "##"  {return tok(sym.HASHHASH, null);}

<YYINITIAL> "+" {return tok(sym.PLUS, null);}
<YYINITIAL> "-" {return tok(sym.MINUS, null);}
<YYINITIAL> "*" {return tok(sym.TIMES, null);}
<YYINITIAL> "/" {return tok(sym.DIVIDE, null);}
<YYINITIAL> "%" {return tok(sym.MODULUS, null);}
<YYINITIAL> "=" {return tok(sym.ASSIGN, null);}
<YYINITIAL> "<" {return tok(sym.LT, null);}
<YYINITIAL> ">" {return tok(sym.GT, null);}
<YYINITIAL> "!" {return tok(sym.NOT, null);}
<YYINITIAL> "~" {return tok(sym.TILDE, null);}
<YYINITIAL> "&" {return tok(sym.BITWISEAND, null);}
<YYINITIAL> "|" {return tok(sym.BWISEOR, null);}
<YYINITIAL> "^" {return tok(sym.BWISEXOR, null);}
<YYINITIAL> "?" {return tok(sym.QUESTION, null);}

<YYINITIAL> "(" {return tok(sym.LPAREN, null);}
<YYINITIAL> ")" {return tok(sym.RPAREN, null);}
<YYINITIAL> "{" {return tok(sym.LBRACE, null);}
<YYINITIAL> "}" {return tok(sym.RBRACE, null);}
<YYINITIAL> "[" {return tok(sym.LBRACK, null);}
<YYINITIAL> "]" {return tok(sym.RBRACK, null);}
<YYINITIAL> ";" {return tok(sym.SEMICOLON, null);}
<YYINITIAL> "," {return tok(sym.COMMA, null);}
<YYINITIAL> ":" {return tok(sym.COLON, null);}
<YYINITIAL> "." {return tok(sym.PERIOD, null);}
<YYINITIAL> "#" {return tok(sym.HASH, null);}

<YYINITIAL> "/*" {commentStart = yychar; yybegin(COMMENT);}
<COMMENT> "*/" {commentStart = -1; yybegin(YYINITIAL);}
<COMMENT> [^*\r\n]+ {}
<COMMENT> "*" {}
<COMMENT> (\r\n|\r|\n) {newline();}

<YYINITIAL> \" {startLiteral(); yybegin(STRING);}
<STRING> \" {
  yybegin(YYINITIAL);
  if (!invalid) {
    java_cup.runtime.Symbol result = literal(sym.STRING_LITERAL);
    start = -1;
    return result;
  }
  start = -1;
}
<STRING> [^\\\"\r\n]+ {sb.append(yytext());}

<YYINITIAL> "'" {startLiteral(); yybegin(CHARACTER);}
<CHARACTER> "'" {
  yybegin(YYINITIAL);
  if (!invalid && sb.length() == 1) {
    java_cup.runtime.Symbol result = literal(sym.CHAR_LITERAL);
    start = -1;
    return result;
  }
  if (!invalid) err(start, "Invalid character literal");
  start = -1;
}
<CHARACTER> [^\\'\r\n]+ {sb.append(yytext());}

<STRING,CHARACTER> \\x[0-9a-fA-F]+ {escape(yytext());}
<STRING,CHARACTER> \\[0-7][0-7]?[0-7]? {escape(yytext());}
<STRING,CHARACTER> \\(\r\n|\r|\n) {newline();}
<STRING,CHARACTER> \\[^\r\n] {escape(yytext());}
<STRING,CHARACTER> (\r\n|\r|\n) {
  err(start, "Unterminated literal");
  start = -1;
  newline();
  yybegin(YYINITIAL);
}

<YYINITIAL> " "+ {}
<YYINITIAL> [\t\f\013]+ {}
<YYINITIAL> (\r\n|\r|\n) {newline();}
<YYINITIAL> . {err("Illegal character: " + yytext());}
