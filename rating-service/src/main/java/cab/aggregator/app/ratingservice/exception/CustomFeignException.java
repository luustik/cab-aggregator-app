package cab.aggregator.app.ratingservice.exception;

public class CustomFeignException extends RuntimeException {

    public CustomFeignException(String message) {
        super(message);
    }
}
