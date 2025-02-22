package cab.aggregator.app.authservice.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Exception DTO")
@Builder
public record ExceptionDto(

        @Schema(description = "Message")
        String message
) {
}
