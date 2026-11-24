package ma.youcode.covoiturage.commun.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleVehiculeNotExists(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Message",ex.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleImmatriculationAlreadyExists(AlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Message",ex.getMessage()));
    }

    @ExceptionHandler(VehiculeConducteurMismatchException.class)
    public ResponseEntity<Map<String, String>> handleVehiculeConducteurMismatch(VehiculeConducteurMismatchException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Message",ex.getMessage()));
    }

    @ExceptionHandler(VehiculeAssocieATrajetException.class)
    public ResponseEntity<Map<String, String>> handleVehiculeAssocieATrajet(VehiculeAssocieATrajetException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Message",ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("Message",ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleTrajetNonDisponible(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Message",ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream().collect(
                        java.util.stream.Collectors.toMap(
                                error -> error.getField(),
                                error -> error.getDefaultMessage(),
                                (existing, replacement) -> existing));

        return ResponseEntity.badRequest().body(errors);
    }
}