package ma.youcode.covoiturage.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                error -> error.getField(),
                                error -> error.getDefaultMessage(),
                                (existing, replacement) -> existing));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(VehiculeAssocieATrajetException.class)
    public ResponseEntity<ProblemDetail> handleVehiculeAssocieATrajet(
            VehiculeAssocieATrajetException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}