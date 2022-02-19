JFLAGS = -g -cp . -d .
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/main/java/token/tokentype/TokenType.java \
	src/main/java/token/tokentype/ScannerTokenType.java \
	src/main/java/token/tokentype/ASTTokenType.java \
	src/main/java/token/tokentype/PredefinedTokenType.java \
	src/main/java/token/tokentype/ValueTokenType.java \
	src/main/java/token/Token.java \
	src/main/java/token/ScannerToken.java \
	src/main/java/exception/InvalidTokenException.java \
	src/main/java/exception/InvalidTokenTypeException.java \
	src/main/java/exception/OutOfOrderTokenException.java \
	src/main/java/token/ASTToken.java \
	src/main/java/token/BasicToken.java \
	src/main/java/util/Util.java \
	src/main/java/util/ProgramReader.java \
	src/main/java/util/Validator.java \
	src/main/java/lex/Scanner.java \
	src/main/java/lex/Screener.java \
	src/main/java/parser/ASTNode.java \
	src/main/java/parser/Parser.java \
	src/main/java/winzigc.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	#find . -name "*.class" -type f -delete