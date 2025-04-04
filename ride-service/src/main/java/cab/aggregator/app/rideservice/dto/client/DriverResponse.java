package cab.aggregator.app.rideservice.dto.client;

public record DriverResponse(

        int id,

        String name,

        String email,

        String phoneNumber,

        String gender,

        boolean deleted
) {
}
