package cab.aggregator.app.ratingservice.dto.client;

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
) {
}
