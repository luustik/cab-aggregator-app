package cab.aggregator.app.rideservice.exception;

public class CustomFeignException extends RuntimeException {

    public CustomFeignException(String message) {
        super(message);
    }
}
