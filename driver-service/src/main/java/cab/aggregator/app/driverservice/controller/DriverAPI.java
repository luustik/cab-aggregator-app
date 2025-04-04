package cab.aggregator.app.driverservice.controller;

import cab.aggregator.app.driverservice.dto.request.DriverRequest;
import cab.aggregator.app.driverservice.dto.request.PasswordRequest;
import cab.aggregator.app.driverservice.dto.response.DriverContainerResponse;
import cab.aggregator.app.driverservice.dto.response.DriverResponse;
import cab.aggregator.app.driverservice.dto.validation.OnCreate;
import cab.aggregator.app.driverservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;

import static cab.aggregator.app.driverservice.utility.RegExp.GENDER_PATTERN;

@Tag(name = "Driver controller")
public interface DriverAPI {
    @Operation(summary = "Get driver dy Id")
    DriverResponse getDriverById(int id);

    @Operation(summary = "Get all drivers for Admin")
    DriverContainerResponse getAllDriversAdmin(@Min(0) int offset,
                                               @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all drivers")
    DriverContainerResponse getAllDrivers(@Min(0) int offset,
                                          @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all drivers by gender")
    DriverContainerResponse getAllDriversByGender(@Valid @Validated
                                                  @Pattern(regexp = GENDER_PATTERN, message = "{gender.pattern}") String gender,
                                                  @Min(0) int offset,
                                                  @Min(1) @Max(100) int limit);

    @Operation(summary = "Safe delete driver by Id")
    void safeDeleteDriverById(int id, JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Hard delete driver by Id")
    void deleteDriverById(int id);

    @Operation(summary = "Create new driver")
    ResponseEntity<DriverResponse> createDriver(@Valid @Validated(OnCreate.class) DriverRequest request);

    @Operation(summary = "Update driver by Id")
    DriverResponse updateDriver(int id, @Valid @Validated(OnUpdate.class) DriverRequest request, JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Update driver password by id")
    void updatePassword(int id, @Valid @Validated(OnUpdate.class) PasswordRequest request, JwtAuthenticationToken jwtAuthenticationToken);
}
