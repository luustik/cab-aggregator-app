package cab.aggregator.app.rideservice.dto.client;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ClientException(

        HttpStatus status,

        String message
) {
}

