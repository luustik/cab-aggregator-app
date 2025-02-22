package cab.aggregator.app.authservice.exception.handler;

import cab.aggregator.app.authservice.dto.exception.ExceptionDto;
import cab.aggregator.app.authservice.dto.exception.MultiException;
import cab.aggregator.app.authservice.exception.CreateUserException;
import cab.aggregator.app.authservice.exception.ExternalClientException;
import cab.aggregator.app.authservice.exception.KeycloakException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static cab.aggregator.app.authservice.util.MessageKeys.DEFAULT_EXCEPTION_KEY;
import static cab.aggregator.app.authservice.util.MessageKeys.VALIDATION_FAILED_KEY;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

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

    @ExceptionHandler(ExternalClientException.class)
    public ResponseEntity<ExceptionDto> handleExternalClient(ExternalClientException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ExceptionDto.builder()
                        .message(e.getMessage())
                        .build());
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
                .message(messageSource.getMessage(VALIDATION_FAILED_KEY, null, LocaleContextHolder.getLocale()))
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
                .message(messageSource.getMessage(VALIDATION_FAILED_KEY, null, LocaleContextHolder.getLocale()))
                .errors(errors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ExceptionDto.builder()
                .message(messageSource.getMessage(DEFAULT_EXCEPTION_KEY, null, LocaleContextHolder.getLocale()))
                .build();
    }
}


