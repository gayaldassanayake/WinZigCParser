package com.cs4542.compiler.token;

import com.cs4542.compiler.token.tokentype.ASTTokenType;

public class ASTToken implements Token {
    private final ASTTokenType type;

    public ASTToken(ASTTokenType type) {
        this.type = type;
    }

    public ASTTokenType getType() {
        return type;
    }
}
