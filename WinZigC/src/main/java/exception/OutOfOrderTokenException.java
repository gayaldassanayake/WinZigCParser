package exception;

import token.ScannerToken;

public class OutOfOrderTokenException extends Exception {
    public OutOfOrderTokenException(ScannerToken token) {
        super("Token "+ token.getValue() +" found out of order");
    }
}
