package compiler.token.tokentype;

public enum ValueTokenType implements ScannerTokenType {
    WHITESPACE,
    NEWLINE,
    IDENTIFIER,
    INTEGER,
    CHAR,
    STRING,
    COMMENT
}
