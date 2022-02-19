package token;


import token.tokentype.TokenType;

public interface Token {
    String getValue();
    TokenType getType();
}
