package com.cs4542.compiler.lex;

import com.cs4542.compiler.token.BasicTokenType;
import com.cs4542.compiler.token.Token;
import com.cs4542.compiler.token.TokenType;
import com.cs4542.compiler.util.Util;

import java.util.ArrayList;

public class Screener {
    private static final ArrayList<Token> processedTokens = new ArrayList<>();

    public static ArrayList<Token> screen (ArrayList<Token> originalTokens) {
        for (Token token: originalTokens) {
            TokenType currentTokenType = token.getType();
            if ((currentTokenType != BasicTokenType.COMMENT) && (currentTokenType != BasicTokenType.WHITESPACE) &&
                    (currentTokenType != BasicTokenType.NEWLINE)) {
                processedTokens.add(token);
            }
        }
        Util.dumpTokens(processedTokens, "screener-tokens.txt");
        return processedTokens;
    }
}
