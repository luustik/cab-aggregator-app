package cab.aggregator.app.rideservice.controller;

import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import cab.aggregator.app.rideservice.dto.validation.OnCreate;
import cab.aggregator.app.rideservice.dto.validation.OnUpdate;
import cab.aggregator.app.rideservice.service.RideService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static cab.aggregator.app.rideservice.utility.RegExp.REGEXP_DATE_TIME;
import static cab.aggregator.app.rideservice.utility.RegExp.REGEXP_STATUS;


@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
@Validated
public class RideControllerImpl implements RideController {

    private final RideService rideService;

    @Override
    @GetMapping("/{id}")
    public RideResponse getRideById(@PathVariable Long id) {
        return rideService.getRideById(id);
    }

    @Override
    @GetMapping
    public RideContainerResponse getAllRides(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                             @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return rideService.getAllRides(offset, limit);
    }

    @Override
    @GetMapping("/driver-id/{driverId}")
    public RideContainerResponse getAllRidesByDriverId(@Valid @Validated
                                                       @PathVariable Long driverId,
                                                       @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                       @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return rideService.getAllRidesByDriverId(driverId, offset, limit);
    }

    @Override
    @GetMapping("/passenger-id/{passengerId}")
    public RideContainerResponse getAllRidesByPassengerId(@Valid @Validated
                                                          @PathVariable Long passengerId,
                                                          @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                          @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return rideService.getAllRidesByPassengerId(passengerId, offset, limit);
    }

    @Override
    @GetMapping("/status/{status}")
    public RideContainerResponse getAllRidesByStatus(@Valid @Validated
                                                     @PathVariable
                                                     @Pattern(regexp = REGEXP_STATUS, message = "{status.pattern}") String status,
                                                     @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                     @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return rideService.getAllRidesByStatus(status, offset, limit);
    }


    @Override
    @GetMapping(value = "/ride-between-date-time/", params = {"start", "end"})
    public RideContainerResponse getAllBetweenOrderDateTime(
            @RequestParam
            @Pattern(regexp = REGEXP_DATE_TIME, message = "{date.pattern}")
            String start,
            @RequestParam
            @Pattern(regexp = REGEXP_DATE_TIME, message = "{date.pattern}")
            String end,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
            @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return rideService.getAllBetweenOrderDateTime(start, end, offset, limit);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRideById(@PathVariable Long id) {
        rideService.deleteRide(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<RideResponse> createRide(@Valid @Validated(OnCreate.class)
                                                   @RequestBody RideRequest request,
                                                   JwtAuthenticationToken token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rideService
                        .createRide(request, token));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RideResponse updateRide(@PathVariable Long id,
                                   @Valid @Validated(OnUpdate.class) @RequestBody RideRequest request) {
        return rideService.updateRide(id, request);
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public RideResponse updateRideStatus(@PathVariable Long id,
                                         @Valid @Validated(OnUpdate.class)
                                         @Pattern(regexp = REGEXP_STATUS, message = "{status.pattern}")
                                         @RequestBody
                                         String status,
                                         JwtAuthenticationToken token) {
        return rideService.updateRideStatus(id, status, token);
    }
}
