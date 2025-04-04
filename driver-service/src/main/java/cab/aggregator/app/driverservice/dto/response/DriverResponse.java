package cab.aggregator.app.driverservice.dto.response;

import java.io.Serializable;

public record DriverResponse(

        int id,

        String name,

        String email,

        String phoneNumber,

        String gender,

        boolean deleted,

        double avgGrade
) implements Serializable {
}
