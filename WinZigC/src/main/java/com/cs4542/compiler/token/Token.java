package com.cs4542.compiler.token;


import com.cs4542.compiler.token.tokentype.TokenType;

public interface Token {
    String getValue();
    TokenType getType();
}
