package compiler.token;


import compiler.token.tokentype.TokenType;

public interface Token {
    String getValue();
    TokenType getType();
}
