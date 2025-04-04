package cab.aggregator.app.ratingservice.dto.client;

public record DriverResponse(

        int id,

        String name,

        String email,

        String phoneNumber,

        String gender,

        boolean deleted
) {
}
