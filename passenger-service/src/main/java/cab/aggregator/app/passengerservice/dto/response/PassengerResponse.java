package cab.aggregator.app.passengerservice.dto.response;

import java.io.Serializable;

public record PassengerResponse(
        int id,

        String name,

        String email,

        String phone,

        boolean deleted,

        double avgGrade
) implements Serializable {
}
