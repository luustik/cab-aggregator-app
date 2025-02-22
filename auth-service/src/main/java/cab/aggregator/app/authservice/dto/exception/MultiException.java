package cab.aggregator.app.authservice.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Map;

@Schema(description = "Multi exception DTO")
@Builder
public record MultiException(

        @Schema(description = "Message")
        String message,

        @Schema(description = "Errors")
        Map<String, String> errors
) {
}
