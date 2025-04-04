package cab.aggregator.app.passengerservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PassengerContainerResponse(

        List<PassengerResponse> items,

        int currentPage,

        int sizePage,

        int countPages,

        long totalElements
) {
}
