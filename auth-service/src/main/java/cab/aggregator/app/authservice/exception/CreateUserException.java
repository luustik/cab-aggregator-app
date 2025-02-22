package cab.aggregator.app.authservice.exception;

import lombok.Getter;

@Getter
public class CreateUserException extends RuntimeException {

    private final int statusCode;

    public CreateUserException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
