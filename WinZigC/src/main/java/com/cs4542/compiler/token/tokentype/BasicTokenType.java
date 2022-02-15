package com.cs4542.compiler.token.tokentype;

public enum BasicTokenType implements ScannerTokenType {
    WHITESPACE,
    NEWLINE,
    IDENTIFIER,
    INTEGER,
    CHAR,
    STRING,
    COMMENT
}
