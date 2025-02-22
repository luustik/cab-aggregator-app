package cab.aggregator.app.ratingservice.exception.handler;

import cab.aggregator.app.ratingservice.dto.exception.ExceptionDto;
import cab.aggregator.app.ratingservice.dto.exception.MultiException;
import cab.aggregator.app.ratingservice.exception.AccessDeniedException;
import cab.aggregator.app.ratingservice.exception.EmptyListException;
import cab.aggregator.app.ratingservice.exception.ExternalClientException;
import cab.aggregator.app.ratingservice.exception.EntityNotFoundException;
import cab.aggregator.app.ratingservice.exception.ResourceAlreadyExistException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static cab.aggregator.app.ratingservice.utility.MessageKeys.DEFAULT_EXCEPTION_KEY;
import static cab.aggregator.app.ratingservice.utility.MessageKeys.VALIDATION_FAILED_KEY;


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

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleEntityNotFound(RuntimeException e) {
        return ExceptionDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleResourceAlreadyExist(RuntimeException e) {
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
        e.printStackTrace();
        return ExceptionDto.builder()
                .message(messageSource.getMessage(DEFAULT_EXCEPTION_KEY, null, LocaleContextHolder.getLocale()))
                .build();
    }
}


