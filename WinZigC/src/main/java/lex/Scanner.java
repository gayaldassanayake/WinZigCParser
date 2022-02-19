package lex;

import token.ScannerToken;
import token.tokentype.ValueTokenType;
import token.tokentype.PredefinedTokenType;
import exception.InvalidTokenException;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Scanner {
    private static int readPointer =0;
    private static final ArrayList<ScannerToken> tokens = new ArrayList<>();
    private static String program;

    private static boolean isPredefinedToken() {
        HashMap<String, PredefinedTokenType> predefinedTokens = PredefinedTokenType.getPredefinedTokenValues();
        for (String token: predefinedTokens.keySet()) {
            int tokenSize = token.length();
            if(readPointer+tokenSize<=program.length() &&
                    program.substring(readPointer, readPointer+tokenSize).equals(token) &&
                    (!isIdentifierHead(program.charAt(readPointer)) ||
                            !isIdentifierTail(program.charAt(readPointer+tokenSize)))) {
                return true;
            }
        }
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
        while(readPointer<program.length() && Util.getCharFromString(program, readPointer).matches(".")) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new ScannerToken(program.substring(startIndex, endIndex), ValueTokenType.COMMENT));
    }

    private static void readMultiLineComment() throws InvalidTokenException {
        int startIndex = readPointer;
        // loop while the character is a '}'.
        while((program.charAt(readPointer) != '}')) {
            readPointer++;
            if (readPointer>=program.length()) {
                throw new InvalidTokenException("comment");
            }
        }
        int endIndex = ++readPointer;
        tokens.add(new ScannerToken(program.substring(startIndex, endIndex), ValueTokenType.COMMENT));
    }

    private static void readWhitespace() {
        int startIndex = readPointer;
        while(readPointer<program.length() && Character.isWhitespace(program.charAt(readPointer))
                && program.charAt(readPointer) != '\n') {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new ScannerToken(program.substring(startIndex, endIndex), ValueTokenType.WHITESPACE));
    }

    private static void readNewLine() {
        tokens.add(new ScannerToken("\n", ValueTokenType.NEWLINE));
        readPointer++;
    }

    private static void readChar() throws InvalidTokenException {
        int startIndex = readPointer;
        readPointer+=2;
        if(program.charAt(readPointer) != '\'') {
            throw new InvalidTokenException("char");
        }
        int endIndex = ++readPointer;
        tokens.add(new ScannerToken(program.substring(startIndex, endIndex), ValueTokenType.CHAR));
    }

    private static void readString() throws InvalidTokenException {
        int startIndex = readPointer;
        readPointer++;
        while(program.charAt(readPointer) != '"') {
            readPointer++;
            if (readPointer>=program.length()) {
                throw new InvalidTokenException("string");
            }
        }
        int endIndex = ++readPointer;
        tokens.add(new ScannerToken(program.substring(startIndex, endIndex), ValueTokenType.STRING));
    }

    private static void readIdentifier() {
        int startIndex = readPointer;
        readPointer++;
        while(readPointer<program.length() && isIdentifierTail(program.charAt(readPointer))) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new ScannerToken(program.substring(startIndex, endIndex), ValueTokenType.IDENTIFIER));
    }

    private static void readInteger() {
        int startIndex = readPointer;
        readPointer++;
        while(readPointer<program.length() && isInteger(program.charAt(readPointer))) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new ScannerToken(program.substring(startIndex, endIndex), ValueTokenType.INTEGER));
    }

    private static void readPredefinedToken() {
        HashMap<String, PredefinedTokenType> predefinedTokens = PredefinedTokenType.getPredefinedTokenValues();
        String currentKey = null;
        for (String key: predefinedTokens.keySet()) {
            int tokenSize = key.length();
            if(readPointer+tokenSize<=program.length() &&
                    program.substring(readPointer, readPointer+tokenSize).equals(key) &&
                    (currentKey==null || currentKey.length()< tokenSize)) {
                currentKey = key;
            }
        }
        if(currentKey!=null) {
            tokens.add(new ScannerToken(currentKey, predefinedTokens.get(currentKey)));
            readPointer+=currentKey.length();
        }
    }

    public static ArrayList<ScannerToken> scan(String program) throws InvalidTokenException {
        Scanner.program = program;
        while (readPointer<program.length()) {
            Character ch = program.charAt(readPointer);
            if(ch.equals('#')) {
                readSingleLineComment();
            } else if(ch.equals('{')){
                readMultiLineComment();
            } else if(Character.isWhitespace(ch) && program.charAt(readPointer) != '\n'){
                readWhitespace();
            } else if(ch.equals('\n')) {
                readNewLine();
            } else if(ch=='\'') {
                readChar();
            } else if(ch.equals('"')) {
                readString();
            } else if(isInteger(ch)) {
                readInteger();
            } else {
                if(isPredefinedToken()) {
                    readPredefinedToken();
                } else if(isIdentifierHead(ch)) {
                    readIdentifier();
                } else {
                    throw new InvalidTokenException("unknown");
                }
            }
        }
        Util.dumpTokens(tokens, "scanner-tokens.txt");
        return tokens;
    }
}
