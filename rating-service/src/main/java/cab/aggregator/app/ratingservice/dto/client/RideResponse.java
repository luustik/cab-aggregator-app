package cab.aggregator.app.ratingservice.dto.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RideResponse(

        long id,

        long driverId,

        long passengerId,

        String departureAddress,

        String arrivalAddress,

        String status,

        LocalDateTime orderDateTime,

        BigDecimal cost
) {
}
