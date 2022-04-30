JFLAGS = -g -cp . -d .
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/main/java/compiler/token/tokentype/TokenType.java \
	src/main/java/compiler/token/tokentype/ScannerTokenType.java \
	src/main/java/compiler/token/tokentype/ASTTokenType.java \
	src/main/java/compiler/token/tokentype/PredefinedTokenType.java \
	src/main/java/compiler/token/tokentype/ValueTokenType.java \
	src/main/java/compiler/token/Token.java \
	src/main/java/compiler/token/ScannerToken.java \
	src/main/java/compiler/exception/InvalidTokenException.java \
	src/main/java/compiler/exception/InvalidTokenTypeException.java \
	src/main/java/compiler/exception/OutOfOrderTokenException.java \
	src/main/java/compiler/token/ASTToken.java \
	src/main/java/compiler/token/BasicToken.java \
	src/main/java/compiler/util/Util.java \
	src/main/java/compiler/util/ProgramReader.java \
	src/main/java/compiler/util/Validator.java \
	src/main/java/compiler/lex/Scanner.java \
	src/main/java/compiler/lex/Screener.java \
	src/main/java/compiler/parser/ASTNode.java \
	src/main/java/compiler/parser/Parser.java \
	src/main/java/winzigc.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	find . -name "*.class" -type f -delete
	$(RM) -r compiler
	find . -name "*-out.txt" -type f -delete
