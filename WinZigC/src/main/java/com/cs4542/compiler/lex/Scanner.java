package com.cs4542.compiler.lex;

import com.cs4542.compiler.token.Token;
import com.cs4542.compiler.token.TokenType;
import com.cs4542.compiler.util.InvalidTokenException;
import com.cs4542.compiler.util.Util;

import java.util.ArrayList;

public class Scanner {
    private static int readPointer =0;
    private static final ArrayList<Token> tokens = new ArrayList<>();
    private static String program;

    private static boolean isPredefinedToken() {
        ArrayList<String> predefinedTokens = Token.getPredefinedTokens();
        for (String token: predefinedTokens) {
            int tokenSize = token.length();
            if(readPointer+tokenSize<=program.length() &&
                    program.substring(readPointer, readPointer+tokenSize).equals(token)) {
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
        tokens.add(new Token(program.substring(startIndex, endIndex), TokenType.COMMENT));
    }

    private static void readMultiLineComment() throws InvalidTokenException {
        int startIndex = readPointer;
        // loop while the character is a '}'.
        while((program.charAt(readPointer) != '}')) {
            readPointer++;
            if (readPointer>=program.length()) {
                throw new InvalidTokenException("Invalid comment token");
            }
        }
        int endIndex = ++readPointer;
        tokens.add(new Token(program.substring(startIndex, endIndex), TokenType.COMMENT));
    }

    private static void readWhitespace() {
        int startIndex = readPointer;
        while(readPointer<program.length() && Character.isWhitespace(program.charAt(readPointer))
                && program.charAt(readPointer) != '\n') {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new Token(program.substring(startIndex, endIndex), TokenType.WHITESPACE));
    }

    private static void readNewLine() {
        tokens.add(new Token("\n", TokenType.NEWLINE));
        readPointer++;
    }

    private static void readChar() throws InvalidTokenException {
        int startIndex = readPointer;
        readPointer+=2;
        if(program.charAt(readPointer) != '\'') {
            throw new InvalidTokenException("Invalid char token");
        }
        int endIndex = ++readPointer;
        tokens.add(new Token(program.substring(startIndex, endIndex), TokenType.CHAR));
    }

    private static void readString() throws InvalidTokenException {
        int startIndex = readPointer;
        readPointer++;
        while(program.charAt(readPointer) != '"') {
            readPointer++;
            if (readPointer>=program.length()) {
                throw new InvalidTokenException("Invalid string token");
            }
        }
        int endIndex = ++readPointer;
        tokens.add(new Token(program.substring(startIndex, endIndex), TokenType.STRING));
    }

    private static void readIdentifier() {
        int startIndex = readPointer;
        readPointer++;
        while(readPointer<program.length() && isIdentifierTail(program.charAt(readPointer))) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new Token(program.substring(startIndex, endIndex), TokenType.IDENTIFIER));
    }

    private static void readInteger() {
        int startIndex = readPointer;
        readPointer++;
        while(readPointer<program.length() && isInteger(program.charAt(readPointer))) {
            readPointer++;
        }
        int endIndex = readPointer;
        tokens.add(new Token(program.substring(startIndex, endIndex), TokenType.INTEGER));
    }

    private static void readPredefinedToken() {
        ArrayList<String> predefinedTokens = Token.getPredefinedTokens();
        for (String token: predefinedTokens) {
            int tokenSize = token.length();
            if(readPointer+tokenSize<=program.length() &&
                    program.substring(readPointer, readPointer+tokenSize).equals(token)) {
                tokens.add(new Token(token, TokenType.PREDEFINED));
                readPointer+=tokenSize;
                break;
            }
        }
    }

    public static ArrayList<Token> scan(String program) throws InvalidTokenException {
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
                    throw new InvalidTokenException("Invalid token found during scanning");
                }
            }
        }
        Util.dumpTokens(tokens, "scanner-tokens.txt");
        return tokens;
    }
}
