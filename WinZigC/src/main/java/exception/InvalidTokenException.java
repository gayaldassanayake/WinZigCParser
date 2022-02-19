package exception;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String token) {
        super("Invalid " + token + " token found during scanning");
    }
}
