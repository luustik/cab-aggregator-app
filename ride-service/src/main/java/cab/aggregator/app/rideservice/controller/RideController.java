package cab.aggregator.app.rideservice.controller;

import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import cab.aggregator.app.rideservice.dto.validation.OnCreate;
import cab.aggregator.app.rideservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;

import static cab.aggregator.app.rideservice.utility.RegExp.REGEXP_DATE_TIME;
import static cab.aggregator.app.rideservice.utility.RegExp.REGEXP_STATUS;

@Tag(name = "Ride Controller")
public interface RideController {

    @Operation(summary = "Get ride by id")
    RideResponse getRideById(Long id);

    @Operation(summary = "Get all rides")
    RideContainerResponse getAllRides(@Min(0) int offset,
                                      @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all rides by driver id")
    RideContainerResponse getAllRidesByDriverId(@Valid @Validated Long driverId, @Min(0) int offset, @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all rides by passenger id")
    RideContainerResponse getAllRidesByPassengerId(@Valid @Validated Long passengerId, @Min(0) int offset, @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all rides by status")
    RideContainerResponse getAllRidesByStatus(@Valid @Validated
                                              @Pattern(regexp = REGEXP_STATUS, message = "{status.pattern}") String status,
                                              @Min(0)
                                              int offset,
                                              @Min(1) @Max(100)
                                              int limit);

    @Operation(summary = "Get all rides for period")
    RideContainerResponse getAllBetweenOrderDateTime(@Pattern(regexp = REGEXP_DATE_TIME, message = "{date.pattern}")
                                                     String start,
                                                     @Pattern(regexp = REGEXP_DATE_TIME, message = "{date.pattern}")
                                                     String end,
                                                     @Min(0)
                                                     int offset,
                                                     @Min(1) @Max(100)
                                                     int limit);

    @Operation(summary = "Delete ride by id")
    void deleteRideById(Long id);

    @Operation(summary = "Create new ride")
    ResponseEntity<RideResponse> createRide(@Valid @Validated(OnCreate.class) RideRequest request, JwtAuthenticationToken token);

    @Operation(summary = "Update ride by id")
    RideResponse updateRide(Long id, @Valid @Validated(OnUpdate.class) RideRequest request);

    @Operation(summary = "Update ride status by id")
    RideResponse updateRideStatus(Long id, @Valid @Validated(OnUpdate.class)
    @Pattern(regexp = REGEXP_STATUS, message = "{status.pattern}") String status, JwtAuthenticationToken token);
}
