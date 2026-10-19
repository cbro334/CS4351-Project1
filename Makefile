JFLAGS=-g
COURSE_CLASSES=/classes/cs4351/cs4351_bau/pub/classes
JAVA_CP=.:$(COURSE_CLASSES):$(COURSE_CLASSES)/chap4

.PHONY: all clean

all: Parse/Main.class

Parse/Main.class: Parse/Grm.java Parse/sym.java Parse/Yylex.java Parse/Main.java Parse/Lexer.java $(wildcard Absyn/*.java)
	javac $(JFLAGS) -cp "$(JAVA_CP)" Absyn/*.java Parse/*.java

Parse/Grm.java Parse/sym.java &: Parse/Grm.cup
	cd Parse && java -cp "$(COURSE_CLASSES)" java_cup.Main -parser Grm -expect 1 -nonterms -compact_red < Grm.cup > Grm.out 2> Grm.err

Parse/Yylex.java: Parse/Tiger.lex
	cd Parse && java -cp "$(COURSE_CLASSES)" JLex.Main Tiger.lex && mv Tiger.lex.java Yylex.java

clean:
	rm -f Absyn/*.class Parse/*.class Parse/Yylex.java Parse/Grm.java Parse/Grm.out Parse/Grm.err
