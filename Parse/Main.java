package Parse;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws IOException {

        for (String filename : args) {

            ErrorMsg.ErrorMsg errorMsg = new ErrorMsg.ErrorMsg(filename);

            InputStream inp = new FileInputStream(filename);
            Lexer lexer = new Yylex(inp, errorMsg);

            java_cup.runtime.Symbol tok;

            do {
                tok = lexer.nextToken();

                String extra = "";

                if (tok.sym == sym.ID) {
                    extra = "\t$" + tok.value;
                }
                else if (tok.sym == sym.DECIMAL_LITERAL) {
                    extra = "\t#" + tok.value;
                }
                else if (tok.sym == sym.STRING_LITERAL) {
                    extra = " \"" + tok.value + "\"";
                }
                else if (tok.sym == sym.CHAR_LITERAL) {
                    extra = " '" + tok.value + "'";
                }

                System.out.println(
                    symnames[tok.sym] + " " + tok.left + extra
                );

            } while (tok.sym != sym.EOF);

            inp.close();
        }
    }

    static String[] symnames = new String[100];

    static {
        for (Field field : sym.class.getDeclaredFields()) {

            if (field.getType() == int.class) {

                try {
                    int value = field.getInt(null);
                    symnames[value] = field.getName();
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
