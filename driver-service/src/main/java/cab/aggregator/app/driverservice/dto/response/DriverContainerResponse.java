package cab.aggregator.app.driverservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record DriverContainerResponse(
        List<DriverResponse> items,

        int currentPage,

        int sizePage,

        int countPages,

        long totalElements
) {
}
