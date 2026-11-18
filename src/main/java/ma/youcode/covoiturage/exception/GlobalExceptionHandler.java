package ma.youcode.covoiturage.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConducteurNotExistsException.class)
    public ResponseEntity<Map<String, String>> handleConducteurNotExists(ConducteurNotExistsException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({EmailAlreadyExistsException.class, NumeroPermisAlreadyExistsException.class})
    public ResponseEntity<Map<String, String>> handleDuplicate(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
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
                                (existing, replacement) -> existing
                        )
                );

        return ResponseEntity.badRequest().body(errors);
    }
}