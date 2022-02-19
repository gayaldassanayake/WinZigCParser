JFLAGS = -g -cp . -d .
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/main/java/com/cs4542/compiler/token/tokentype/TokenType.java \
	src/main/java/com/cs4542/compiler/token/tokentype/ScannerTokenType.java \
	src/main/java/com/cs4542/compiler/token/tokentype/ASTTokenType.java \
	src/main/java/com/cs4542/compiler/token/tokentype/PredefinedTokenType.java \
	src/main/java/com/cs4542/compiler/token/tokentype/ValueTokenType.java \
	src/main/java/com/cs4542/compiler/token/Token.java \
	src/main/java/com/cs4542/compiler/token/ScannerToken.java \
	src/main/java/com/cs4542/compiler/exception/InvalidTokenException.java \
	src/main/java/com/cs4542/compiler/exception/InvalidTokenTypeException.java \
	src/main/java/com/cs4542/compiler/exception/OutOfOrderTokenException.java \
	src/main/java/com/cs4542/compiler/token/ASTToken.java \
	src/main/java/com/cs4542/compiler/token/BasicToken.java \
	src/main/java/com/cs4542/compiler/util/Util.java \
	src/main/java/com/cs4542/compiler/util/ProgramReader.java \
	src/main/java/com/cs4542/compiler/util/Validator.java \
	src/main/java/com/cs4542/compiler/lex/Scanner.java \
	src/main/java/com/cs4542/compiler/lex/Screener.java \
	src/main/java/com/cs4542/compiler/parser/ASTNode.java \
	src/main/java/com/cs4542/compiler/parser/Parser.java \
	src/main/java/com/cs4542/compiler/winzigc.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	find . -name "*.class" -type f -delete