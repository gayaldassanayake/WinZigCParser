package com.cs4542.compiler.exception;

import com.cs4542.compiler.token.ScannerToken;

public class OutOfOrderTokenOrderException extends Exception {
    public OutOfOrderTokenOrderException(ScannerToken token) {
        super("Token "+ token.getValue() +" found out of order");
    }
}
