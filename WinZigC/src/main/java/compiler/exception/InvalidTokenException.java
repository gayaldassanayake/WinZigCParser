package compiler.exception;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String token) {
        super("Invalid " + token + " compiler.token found during scanning");
    }
}
