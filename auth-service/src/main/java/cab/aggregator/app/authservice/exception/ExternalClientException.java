package cab.aggregator.app.authservice.exception;

import lombok.Getter;

@Getter
public class ExternalClientException extends RuntimeException {

    private final int statusCode;

    public ExternalClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
