package com.cs4542.compiler.token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Token {
    private static final ArrayList<String> predefinedTokens = new ArrayList<>();
    private final String value;
    private final TokenType type;

    private static void initializePredefTokens() throws IOException {
        BufferedReader bufReader = new BufferedReader(new FileReader("src/main/resources/predeftokens.txt"));

        String line = bufReader.readLine().strip();
        while (line != null) {
            predefinedTokens.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();
    }
    static {
        try {
            initializePredefTokens();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getPredefinedTokens() {
        return predefinedTokens;
    }

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }
}
