package com.cs4542.compiler.util;

import com.cs4542.compiler.token.Token;

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

    public static void dumpTokens(ArrayList<Token> tokens, String filepath){
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write("Lexical Analyzer Token Dump.\n");
            int index = 0;
            for(Token token: tokens) {
                writer.write(
                        (index++) +"\t"+convertEscapeCharsToPrintable(token.getValue())+"\t"+token.getType()+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
