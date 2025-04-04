package cab.aggregator.app.rideservice.dto.request;

import cab.aggregator.app.rideservice.dto.validation.OnCreate;
import cab.aggregator.app.rideservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Ride Request DTO")
public record RideRequest(
        @Schema(description = "Driver id")
        @Positive
        Long driverId,

        @Schema(description = "Passenger id")
        @NotNull(message = "{passengerId.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Positive
        Long passengerId,

        @Schema(description = "Departure address", example = "Independency 1")
        @NotNull(message = "{departureAddress.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{departureAddress.length}", groups = {OnCreate.class, OnUpdate.class})
        String departureAddress,

        @Schema(description = "Arrival address", example = "Independency 1")
        @NotNull(message = "{arrivalAddress.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{arrivalAddress.length}", groups = {OnCreate.class, OnUpdate.class})
        String arrivalAddress

) {
}
