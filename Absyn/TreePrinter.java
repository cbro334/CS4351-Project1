package Absyn;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TreePrinter {
    private final PrintWriter out;

    public TreePrinter(PrintWriter out) {
        this.out = out;
    }

    public void print(Absyn tree) {
        printValue(tree, 0);
        out.flush();
    }

    private void indent(int depth) {
        for (int i = 0; i < depth; i++) {
            out.print("  ");
        }
    }

    private void printValue(Object value, int depth) {
        if (value == null) {
            out.println("null");
            return;
        }

        if (value instanceof IntExp) {
            IntExp literal = (IntExp) value;
            out.println("IntExp(" +
                (literal.lexeme != null ? literal.lexeme : literal.value) + ")");
            return;
        }

        if (!value.getClass().getPackageName().equals("Absyn")) {
            out.println(value);
            return;
        }

        out.println(value.getClass().getSimpleName() + " {");

        for (Field field : value.getClass().getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            indent(depth + 1);
            out.print(field.getName() + ": ");

            try {
                printValue(field.get(value), depth + 1);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        indent(depth);
        out.println("}");
    }
}
