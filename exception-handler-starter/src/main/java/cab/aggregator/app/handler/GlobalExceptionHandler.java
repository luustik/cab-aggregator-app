package cab.aggregator.app.handler;

import cab.aggregator.app.dto.ExceptionDto;
import cab.aggregator.app.dto.MultiException;
import cab.aggregator.app.exception.authservice.CreateUserException;
import cab.aggregator.app.exception.authservice.KeycloakException;
import cab.aggregator.app.exception.common.AccessDeniedException;
import cab.aggregator.app.exception.common.EntityNotFoundException;
import cab.aggregator.app.exception.common.ExternalClientException;
import cab.aggregator.app.exception.common.ResourceAlreadyExistsException;
import cab.aggregator.app.exception.ratingservice.EmptyListException;
import cab.aggregator.app.exception.rideservice.ImpossibleStatusException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static cab.aggregator.app.util.Constants.DEFAULT_EXCEPTION_KEY;
import static cab.aggregator.app.util.Constants.VALIDATION_FAILED_KEY;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDto handleAccessDenied(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<ExceptionDto> handleCreateUser(CreateUserException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ExceptionDto.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ExceptionDto> handleCreateUser(KeycloakException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ExceptionDto.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ImpossibleStatusException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleImpossibleStatus(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleEntityNotFound(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleResourceAlreadyExists(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ExternalClientException.class)
    public ResponseEntity<ExceptionDto> handleExternalClient(ExternalClientException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ExceptionDto.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({IllegalStateException.class, EmptyListException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleIllegalState(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MultiException handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return MultiException.builder()
                .message(messageSource.getMessage(VALIDATION_FAILED_KEY, null, Locale.getDefault()))
                .errors(errors)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MultiException handleConstraintViolation(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return MultiException.builder()
                .message(messageSource.getMessage(VALIDATION_FAILED_KEY, null, Locale.getDefault()))
                .errors(errors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ExceptionDto.builder()
                .message(messageSource.getMessage(DEFAULT_EXCEPTION_KEY, null, Locale.getDefault()))
                .build();
    }
}

