package cab.aggregator.app.rideservice.exception;

import cab.aggregator.app.rideservice.dto.client.ClientException;
import lombok.Getter;

@Getter
public class ExternalClientException extends RuntimeException {

    private final ClientException clientException;

    public ExternalClientException(ClientException clientException) {
        this.clientException = clientException;
    }
}
