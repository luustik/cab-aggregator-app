package cab.aggregator.app.rideservice.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RideResponse(

        Long id,

        Long driverId,

        Long passengerId,

        String departureAddress,

        String arrivalAddress,

        String status,

        LocalDateTime orderDateTime,

        BigDecimal cost
) implements Serializable {
}
