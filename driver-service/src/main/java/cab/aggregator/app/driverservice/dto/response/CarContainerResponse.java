package cab.aggregator.app.driverservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CarContainerResponse(
        List<CarResponse> items
) {
}