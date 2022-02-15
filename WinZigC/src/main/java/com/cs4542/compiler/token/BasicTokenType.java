package com.cs4542.compiler.token;

public enum BasicTokenType implements TokenType {
    WHITESPACE,
    NEWLINE,
    IDENTIFIER,
    INTEGER,
    CHAR,
    STRING,
    COMMENT
}
