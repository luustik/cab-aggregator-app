package cab.aggregator.app.ratingservice.exception;

import cab.aggregator.app.ratingservice.dto.client.ClientException;
import lombok.Getter;

@Getter
public class ExternalClientException extends RuntimeException {

    private final ClientException clientException;

    public ExternalClientException(ClientException clientException) {
        this.clientException = clientException;
    }
}
