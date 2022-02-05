package com.cs4542.compiler.util;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String errorMessage) {
        super(errorMessage);
    }
}
