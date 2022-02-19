package util;

import exception.InvalidTokenTypeException;
import token.ScannerToken;
import token.tokentype.ASTTokenType;
import token.tokentype.PredefinedTokenType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Util {
    public static String convertCharListToString(ArrayList<Character> charList) {
        StringBuilder builder = new StringBuilder(charList.size());
        for(Character ch: charList) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public static ArrayList<Character> convertStringToCharList(String str) {
        ArrayList<Character> charList = new ArrayList<>();
        for (char ch : str.toCharArray()) {
            charList.add(ch);
        }
        return charList;
    }

    public static String getCharFromString(String str, int index) {
        return index<str.length()? str.substring(index, index+1): "";
    }

    public static String convertEscapeCharsToPrintable(String str) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i< str.length(); i++) {
            if (str.charAt(i) == '\n') {
                builder.append("\\n");
            } else if (str.charAt(i) == '\t') {
                builder.append("\\t");
            } else if (str.charAt(i) == '\b') {
                builder.append("\\b");
            } else if (str.charAt(i) == '\r') {
                builder.append("\\r");
            } else if (str.charAt(i) == '\f') {
                builder.append("\\f");
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

    public static void dumpTokens(ArrayList<ScannerToken> tokens, String filepath){
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write("Lexical Analyzer Token Dump.\n");
            int index = 0;
            for(ScannerToken token: tokens) {
                writer.write(
                        (index++) +"\t"+convertEscapeCharsToPrintable(token.getValue())+"\t"+token.getType()+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String buildRepeatedCharString(char c, int n) {
        StringBuilder outputBuffer = new StringBuilder(2*n);
        for (int i = 0; i < n; i++){
            outputBuffer.append(c);
            outputBuffer.append(" ");
        }
        return outputBuffer.toString();
    }

    public static ASTTokenType getASTTypeForPredefType(PredefinedTokenType predefType)
            throws InvalidTokenTypeException {
        switch (predefType) {
            case T_PLUS:
                return ASTTokenType.PLUS;
            case T_MINUS:
                return ASTTokenType.MINUS;
            case T_MULTIPLY:
                return ASTTokenType.MULTIPLY;
            case T_DIVIDE:
                return ASTTokenType.DIVIDE;
            case T_OR:
                return ASTTokenType.OR;
            case T_AND:
                return ASTTokenType.AND;
            case T_NOT:
                return ASTTokenType.NOT;
            case T_MOD:
                return ASTTokenType.MOD;
            case T_LTE:
                return ASTTokenType.LTE;
            case T_LT:
                return ASTTokenType.LT;
            case T_GTE:
                return ASTTokenType.GTE;
            case T_GT:
                return ASTTokenType.GT;
            case T_EQUAL:
                return ASTTokenType.EQUAL;
            case T_NE:
                return ASTTokenType.NOTEQUAL;
            case T_SUCC:
                return ASTTokenType.SUCC;
            case T_PRED:
                return ASTTokenType.PRED;
            case T_CHR:
                return ASTTokenType.CHR;
            case T_ORD:
                return ASTTokenType.ORD;
            default:
                throw new InvalidTokenTypeException(predefType);
        }
    }
}
