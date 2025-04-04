package cab.aggregator.app.ratingservice.dto.kafka;

public record AvgRatingUserResponse(

        int id,

        double avgRating
) {
}
