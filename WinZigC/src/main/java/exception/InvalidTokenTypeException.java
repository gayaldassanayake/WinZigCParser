package exception;

import token.tokentype.TokenType;

public class InvalidTokenTypeException extends Exception {
    public InvalidTokenTypeException(TokenType type) {
        super("Invalid " + type + " token for the token");
    }
}
