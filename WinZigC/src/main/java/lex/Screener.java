package lex;

import token.ScannerToken;
import token.tokentype.ValueTokenType;
import token.tokentype.TokenType;
import util.Util;

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
