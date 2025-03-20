package cab.aggregator.app.driverservice.dto.response;

import java.io.Serializable;

public record CarResponse(

        int id,

        String color,

        String model,

        String carNumber,

        Integer driverId

) implements Serializable {
}
