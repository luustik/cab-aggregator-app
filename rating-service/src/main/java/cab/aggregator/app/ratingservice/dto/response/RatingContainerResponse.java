package cab.aggregator.app.ratingservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RatingContainerResponse(

        List<RatingResponse> items,

        int currentPage,

        int sizePage,

        int countPages,

        long totalElements
) {
}
