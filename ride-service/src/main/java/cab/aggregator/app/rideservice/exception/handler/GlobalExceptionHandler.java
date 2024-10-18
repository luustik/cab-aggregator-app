package cab.aggregator.app.rideservice.exception.handler;

import cab.aggregator.app.rideservice.dto.client.ClientException;
import cab.aggregator.app.rideservice.dto.exception.ExceptionDto;
import cab.aggregator.app.rideservice.dto.exception.MultiException;
import cab.aggregator.app.rideservice.exception.ExternalClientException;
import cab.aggregator.app.rideservice.exception.EntityNotFoundException;
import cab.aggregator.app.rideservice.exception.ImpossibleStatusException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static cab.aggregator.app.rideservice.utility.Constants.DEFAULT_EXCEPTION_MESSAGE;
import static cab.aggregator.app.rideservice.utility.Constants.VALIDATION_FAILED_MESSAGE;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleEntityNotFound(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ImpossibleStatusException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleImpossibleStatus(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ExternalClientException.class)
    public ResponseEntity<ClientException> handleExternalClient(ExternalClientException e) {
        ClientException clientException = e.getClientException();
        return ResponseEntity.status(clientException.status())
                .body(clientException);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBadRequest(RuntimeException e) {
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
                .message(messageSource.getMessage(VALIDATION_FAILED_MESSAGE, null, Locale.getDefault()))
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
                .message(messageSource.getMessage(VALIDATION_FAILED_MESSAGE, null, Locale.getDefault()))
                .errors(errors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleException(Exception e) {
        return ExceptionDto.builder()
                .message(DEFAULT_EXCEPTION_MESSAGE)
                .build();
    }
}

