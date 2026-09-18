package Parse;
import ErrorMsg.ErrorMsg;

%% 

%implements Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char

%{
private void newline() {
  errorMsg.newline(yychar);
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

private ErrorMsg errorMsg;

Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}

%}

%eofval{
	{
	 return tok(sym.EOF, null);
        }
%eofval}       


%%
<YYINITIAL> "int"   {return tok(sym.INT, null);}
<YYINITIAL> ">"     {return tok(sym.GT, null);}
<YYINITIAL> "/"     {return tok(sym.DIVIDE, null);}
<YYINITIAL> ":"     {return tok(sym.COLON, null);}
<YYINITIAL> "else"  {return tok(sym.ELSE, null);}
<YYINITIAL> "or"    {return tok(sym.OR, null);}
<YYINITIAL> "do"    {return tok(sym.DO, null);}
<YYINITIAL> ">="    {return tok(sym.GE, null);}
<YYINITIAL> "error" {return tok(sym.error, null);}
<YYINITIAL> "return" {return tok(sym.RETURN, null);}
<YYINITIAL> "<"     {return tok(sym.LT, null);}
<YYINITIAL> "-"     {return tok(sym.MINUS, null);}
<YYINITIAL> "for"   {return tok(sym.FOR, null);}
<YYINITIAL> "*"     {return tok(sym.TIMES, null);}
<YYINITIAL> ","     {return tok(sym.COMMA, null);}
<YYINITIAL> "<="    {return tok(sym.LE, null);}
<YYINITIAL> "="     {return tok(sym.ASSIGN, null);}
<YYINITIAL> "("     {return tok(sym.LPAREN, null);}
<YYINITIAL> ")"     {return tok(sym.RPAREN, null);}
<YYINITIAL> "if"    {return tok(sym.IF, null);}
<YYINITIAL> ";"     {return tok(sym.SEMICOLON, null);}
<YYINITIAL> "while" {return tok(sym.WHILE, null);}
<YYINITIAL> "["     {return tok(sym.LBRACK, null);}
<YYINITIAL> "]"     {return tok(sym.RBRACK, null);}
<YYINITIAL> "!="    {return tok(sym.NEQ, null);}
<YYINITIAL> "var"   {return tok(sym.VAR, null);}
<YYINITIAL> "break" {return tok(sym.BREAK, null);}
<YYINITIAL> "and"   {return tok(sym.AND, null);}
<YYINITIAL> "+"     {return tok(sym.PLUS, null);}
<YYINITIAL> "{"     {return tok(sym.LBRACE, null);}
<YYINITIAL> "}"     {return tok(sym.RBRACE, null);}
<YYINITIAL> "fun"   {return tok(sym.FUN, null);}
<YYINITIAL> "."     {return tok(sym.PERIOD, null);}
<YYINITIAL> "=="    {return tok(sym.EQ, null);}
<YYINITIAL> [A-Za-z_][A-Za-z0-9_]* {return tok(sym.ID, yytext());}
<YYINITIAL> [0-9]+ {return tok(sym.DECIMAL_LITERAL, Integer.valueOf(yytext()));}

" "	{}
\n	{newline();}
","	{return tok(sym.COMMA, null);}
. { err("Illegal character: " + yytext()); }
