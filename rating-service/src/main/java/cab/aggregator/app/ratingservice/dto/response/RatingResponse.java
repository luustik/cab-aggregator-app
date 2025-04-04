package cab.aggregator.app.ratingservice.dto.response;

import java.io.Serializable;

public record RatingResponse(

        Long id,

        Long rideId,

        Long userId,

        Integer rating,

        String comment,

        String userRole
) implements Serializable {
}
