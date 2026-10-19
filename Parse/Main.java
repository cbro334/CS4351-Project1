package Parse;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java Parse.Main <file.c>");
            System.exit(1);
        }

        boolean failed = false;

        for (String filename : args) {
            ErrorMsg.ErrorMsg errors = new ErrorMsg.ErrorMsg(filename);

            try (InputStream input = new FileInputStream(filename)) {
                Lexer lexer = new Yylex(input, errors);
                Grm parser = new Grm(lexer, errors);

                java_cup.runtime.Symbol result = parser.parse();

                if (errors.anyErrors) {
                    failed = true;
                } else {
                    Absyn.Absyn tree = (Absyn.Absyn) result.value;
                    new Absyn.TreePrinter(
                        new PrintWriter(System.out, true)
                    ).print(tree);
                }
            } catch (Exception e) {
                System.err.println(filename + ": parsing failed: " + e.getMessage());
                failed = true;
            }
        }

        if (failed) {
            System.exit(1);
        }
    }
}
