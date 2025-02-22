package cab.aggregator.app.authservice.dto.response;

public record DriverResponse(

        int id,

        String name,

        String email,

        String phoneNumber,

        String gender,

        boolean deleted,

        double avgGrade
) {
}
