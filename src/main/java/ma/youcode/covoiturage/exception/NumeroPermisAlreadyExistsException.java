package ma.youcode.covoiturage.exception;

public class NumeroPermisAlreadyExistsException extends RuntimeException {

    public NumeroPermisAlreadyExistsException(String message) {
        super(message);
    }
}