package ma.youcode.covoiturage.exception;

public class ConducteurNotExistsException extends RuntimeException{
    public ConducteurNotExistsException(String message){
        super(message);
    }
}
