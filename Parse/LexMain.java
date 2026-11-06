package Parse;

import java.io.PrintWriter;



public class LexMain {



  public static void main(String argv[]) throws java.io.IOException {

    for (int i = 0; i < argv.length; ++i) {

      String filename = argv[i];

      if (argv.length > 1)

	System.out.println("***Processing: " + filename);

      ErrorMsg.ErrorMsg errorMsg = new ErrorMsg.ErrorMsg(filename);

      java.io.InputStream inp=new java.io.FileInputStream(filename);

      Lexer lexer = new Yylex(inp,errorMsg);

      java_cup.runtime.Symbol tok;



      do {

	 String extra = "";

         tok=lexer.nextToken();

	 switch (tok.sym) {

	 case sym.ID:     extra = "\t$" + tok.value; break;

	 case sym.DECIMAL_LITERAL:    extra = "\t#" + tok.value; break;

	 case sym.STRING_LITERAL: extra = " \"" + tok.value + "\""; break;

	 }

	 System.out.println(symnames[tok.sym] + " " + tok.left + extra);

      } while (tok.sym != sym.EOF);



      inp.close();

    }

  }



static String symnames[] = new String[100]; 
 static {
symnames[sym.SHORT] = "SHORT";
symnames[sym.TIMES] = "TIMES";
symnames[sym.GT] = "GT";
symnames[sym.ARROW] = "ARROW";
symnames[sym.DIVASSIGN] = "DIVASSIGN";
symnames[sym.CONST] = "CONST";
symnames[sym.GE] = "GE";
symnames[sym.PERIOD] = "PERIOD";
symnames[sym.REGISTER] = "REGISTER";
symnames[sym.ENUM] = "ENUM";
symnames[sym.RBRACK] = "RBRACK";
symnames[sym.SIZEOF] = "SIZEOF";
symnames[sym.COMMA] = "COMMA";
symnames[sym.RBRACE] = "RBRACE";
symnames[sym.RPAREN] = "RPAREN";
symnames[sym.LBRACK] = "LBRACK";
symnames[sym.NOT] = "NOT";
symnames[sym.LT] = "LT";
symnames[sym.INCREMENT] = "INCREMENT";
symnames[sym.SUBASSIGN] = "SUBASSIGN";
symnames[sym.DOUBLE] = "DOUBLE";
symnames[sym.BWISEANDASSIGN] = "BWISEANDASSIGN";
symnames[sym.STRUCT] = "STRUCT";
symnames[sym.LBRACE] = "LBRACE";
symnames[sym.LPAREN] = "LPAREN";
symnames[sym.MODASSIGN] = "MODASSIGN";
symnames[sym.TILDE] = "TILDE";
symnames[sym.LE] = "LE";
symnames[sym.VAR] = "VAR";
symnames[sym.BITWISEAND] = "BITWISEAND";
symnames[sym.FLOAT] = "FLOAT";
symnames[sym.GOTO] = "GOTO";
symnames[sym.EQ] = "EQ";
symnames[sym.LSHIFTASSIGN] = "LSHIFTASSIGN";
symnames[sym.MODULUS] = "MODULUS";
symnames[sym.LONG] = "LONG";
symnames[sym.PLUS] = "PLUS";
symnames[sym.DIVIDE] = "DIVIDE";
symnames[sym.WHILE] = "WHILE";
symnames[sym.UNION] = "UNION";
symnames[sym.ASSIGN] = "ASSIGN";
symnames[sym.CHAR] = "CHAR";
symnames[sym.ADDASSIGN] = "ADDASSIGN";
symnames[sym.DO] = "DO";
symnames[sym.FOR] = "FOR";
symnames[sym.VOID] = "VOID";
symnames[sym.EXTERN] = "EXTERN";
symnames[sym.RETURN] = "RETURN";
symnames[sym.ELSE] = "ELSE";
symnames[sym.BREAK] = "BREAK";
symnames[sym.FUN] = "FUN";
symnames[sym.INT] = "INT";
symnames[sym.STRING_LITERAL] = "STRING_LITERAL";
symnames[sym.EOF] = "EOF";
symnames[sym.SEMICOLON] = "SEMICOLON";
symnames[sym.ELIPSES] = "ELIPSES";
symnames[sym.MULASSIGN] = "MULASSIGN";
symnames[sym.DECREMENT] = "DECREMENT";
symnames[sym.MINUS] = "MINUS";
symnames[sym.OR] = "OR";
symnames[sym.error] = "error";
symnames[sym.DECIMAL_LITERAL] = "DECIMAL_LITERAL";
symnames[sym.CONTINUE] = "CONTINUE";
symnames[sym.IF] = "IF";
symnames[sym.BWISEOR] = "BWISEOR";
symnames[sym.ID] = "ID";
symnames[sym.COLON] = "COLON";
symnames[sym.BWISEXOR] = "BWISEXOR";
symnames[sym.BWISEORASSIGN] = "BWISEORASSIGN";
symnames[sym.RSHIFTASSIGN] = "RSHIFTASSIGN";
symnames[sym.VOLATILE] = "VOLATILE";
symnames[sym.CHAR_LITERAL] = "CHAR_LITERAL";
symnames[sym.RSHIFT] = "RSHIFT";
symnames[sym.BWISEXORASSIGN] = "BWISEXORASSIGN";
symnames[sym.NEQ] = "NEQ";
symnames[sym.AND] = "AND";
symnames[sym.STATIC] = "STATIC";
symnames[sym.LSHIFT] = "LSHIFT";
symnames[sym.TYPEDEF] = "TYPEDEF";
symnames[sym.AUTO] = "AUTO";
}}
