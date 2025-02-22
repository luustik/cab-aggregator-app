package cab.aggregator.app.authservice.dto.response;

public record PassengerResponse(
        int id,

        String name,

        String email,

        String phone,

        boolean deleted,

        double avgGrade
) {
}
