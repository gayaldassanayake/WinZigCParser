package com.cs4542.compiler.lex;

import com.cs4542.compiler.token.ScannerToken;
import com.cs4542.compiler.token.tokentype.ValueTokenType;
import com.cs4542.compiler.token.tokentype.TokenType;
import com.cs4542.compiler.util.Util;

import java.util.ArrayList;

public class Screener {
    private static final ArrayList<ScannerToken> processedTokens = new ArrayList<>();

    public static ArrayList<ScannerToken> screen (ArrayList<ScannerToken> originalTokens) {
        for (ScannerToken token: originalTokens) {
            TokenType currentTokenType = token.getType();
            if ((currentTokenType != ValueTokenType.COMMENT) && (currentTokenType != ValueTokenType.WHITESPACE) &&
                    (currentTokenType != ValueTokenType.NEWLINE)) {
                processedTokens.add(token);
            }
        }
        Util.dumpTokens(processedTokens, "screener-tokens.txt");
        return processedTokens;
    }
}
