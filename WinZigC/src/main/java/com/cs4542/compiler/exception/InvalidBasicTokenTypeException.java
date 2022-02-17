package com.cs4542.compiler.exception;

import com.cs4542.compiler.token.tokentype.ValueTokenType;

public class InvalidBasicTokenTypeException extends Exception {
    public InvalidBasicTokenTypeException(ValueTokenType type) {
        super("Invalid " + type + " token found during scanning");
    }
}
