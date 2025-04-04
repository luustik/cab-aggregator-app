package cab.aggregator.app.dto;

import lombok.Builder;

@Builder
public record ExceptionDto(

        String message
) {
}
