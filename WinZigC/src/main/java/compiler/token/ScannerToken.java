package compiler.token;

import compiler.token.tokentype.ScannerTokenType;

public class ScannerToken implements Token {
    private final String value;
    private final ScannerTokenType type;

    public ScannerToken(String value, ScannerTokenType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public ScannerTokenType getType() {
        return type;
    }
}
