package com.cs4542.compiler.exception;

import com.cs4542.compiler.token.tokentype.TokenType;

public class InvalidTokenTypeException extends Exception {
    public InvalidTokenTypeException(TokenType type) {
        super("Invalid " + type + " token for the token");
    }
}
