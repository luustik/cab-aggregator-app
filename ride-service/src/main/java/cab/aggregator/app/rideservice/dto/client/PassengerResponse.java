package cab.aggregator.app.rideservice.dto.client;

public record PassengerResponse(
        int id,

        String name,

        String email,

        String phone,

        boolean deleted
) {
}
