package cab.aggregator.app.exception.authservice;

import lombok.Getter;

@Getter
public class CreateUserException extends RuntimeException {

    private final int statusCode;

    public CreateUserException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
