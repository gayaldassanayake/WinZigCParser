package compiler.exception;

import compiler.token.tokentype.TokenType;

public class InvalidTokenTypeException extends Exception {
    public InvalidTokenTypeException(TokenType type) {
        super("Invalid " + type + " compiler.token for the compiler.token");
    }
}
