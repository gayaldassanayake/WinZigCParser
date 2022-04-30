package compiler.lex;

import compiler.token.ScannerToken;
import compiler.token.tokentype.ValueTokenType;
import compiler.token.tokentype.TokenType;
import compiler.util.Util;

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
        Util.dumpTokens(processedTokens, "screener-tokens-out.txt");
        return processedTokens;
    }
}
