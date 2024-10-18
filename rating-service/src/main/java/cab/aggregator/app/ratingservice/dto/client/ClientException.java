package cab.aggregator.app.ratingservice.dto.client;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ClientException(

        HttpStatus status,

        String message
) {
}
