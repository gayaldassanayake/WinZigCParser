import java.util.ArrayList;

public class LexicalAnalyzer {
    private static int readPointer =0;
    private static final ArrayList<Token> tokens = new ArrayList<>();
    private static final ArrayList<Character> program = new ArrayList<>();

    private static void convertProgramToCharList(String str) {
        for (char ch : str.toCharArray()) {
            program.add(ch);
        }
    }

    private static boolean isPredefinedToken() {
        // TODO: complete the logic
        int initialPointer = readPointer;

        readPointer = initialPointer;
        return false;
    }

    private static boolean isIdentifierHead(Character ch) {
        return Character.isLetter(ch) || ch.equals('_');
    }

    private static boolean isIdentifierTail(Character ch) {
        return Character.isLetterOrDigit(ch) || ch.equals('_');
    }

    private static boolean isInteger(Character ch) {
        return Character.isDigit(ch);
    }

    private static void readSingleLineComment() {
        int startIndex = readPointer;
        // loop while the character is not a line break.
        while(readPointer<program.size() && String.valueOf(program.get(readPointer)).matches(".")) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new Token(program.subList(startIndex, endIndex).toString(), TokenType.COMMENT));
    }

    private static void readMultiLineComment() {
        int startIndex = readPointer;
        // loop while the character is a '}'.
        while(readPointer<program.size() && !program.get(readPointer).equals('}')) {
            readPointer++;
        }
        int endIndex = ++readPointer;
        tokens.add(new Token(program.subList(startIndex, endIndex).toString(), TokenType.COMMENT));
    }

    private static void readWhitespace() {
        int startIndex = readPointer;
        while(readPointer<program.size() && Character.isWhitespace(program.get(readPointer))) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new Token(program.subList(startIndex, endIndex).toString(), TokenType.WHITESPACE));
    }

    private static void readChar() {
        int startIndex = readPointer;
        readPointer+=2;
        if(!program.get(readPointer).equals('\'')) {
            throw new Error("Erroneous char");
        }
        int endIndex = ++readPointer;
        tokens.add(new Token(program.subList(startIndex, endIndex).toString(), TokenType.CHAR));
    }

    private static void readString() {
        int startIndex = readPointer;
        readPointer++;
        while(readPointer<program.size() && !program.get(readPointer).equals('"')) {
            readPointer++;
        }
        int endIndex = ++readPointer;
        tokens.add(new Token(program.subList(startIndex, endIndex).toString(), TokenType.STRING));
    }

    private static void readIdentifier() {
        int startIndex = readPointer;
        readPointer++;
        while(readPointer<program.size() && isIdentifierTail(program.get(readPointer))) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new Token(program.subList(startIndex, endIndex).toString(), TokenType.IDENTIFIER));
    }

    private static void readInteger() {
        int startIndex = readPointer;
        readPointer++;
        while(readPointer<program.size() && isInteger(program.get(readPointer))) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new Token(program.subList(startIndex, endIndex).toString(), TokenType.INTEGER));
    }

    public static ArrayList<Token> scan(String programStr) throws Exception {
        // TODO: handle readPointer exceeding the program size with proper error
        convertProgramToCharList(programStr);
        while (readPointer<program.size()) {
            Character ch = program.get(readPointer);
            if(ch.equals('#')) {
                readSingleLineComment();
            } else if(ch.equals('{')){
                readMultiLineComment();
            } else if(Character.isWhitespace(ch)){
                readWhitespace();
            } else if(ch=='\'') {
                readChar();
            } else if(ch.equals('"')) {
                readString();
            } else if(isInteger(ch)) {
                readInteger();
            } else {
                if(isPredefinedToken()) {}
                else if(isIdentifierHead(ch)) {
                    readIdentifier();
                }
                readPointer++;
                System.out.println("Ignore");
//              throw new Exception("Invalid token found during scanning");
            }
        }
        // Break the program into tokens.
        // handle identifier
        // handle white space
        // handle char
        // handle string
        // handle comment
        // handle predef tokens

        return tokens;
    }
}
