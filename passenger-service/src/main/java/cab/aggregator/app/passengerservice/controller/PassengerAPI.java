package cab.aggregator.app.passengerservice.controller;

import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.dto.request.PasswordRequest;
import cab.aggregator.app.passengerservice.dto.response.PassengerContainerResponse;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import cab.aggregator.app.passengerservice.dto.validation.OnCreate;
import cab.aggregator.app.passengerservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;

import static cab.aggregator.app.passengerservice.utility.Constants.EMAIL_PATTERN;
import static cab.aggregator.app.passengerservice.utility.Constants.PHONE_NUMBER_PATTERN;

@Tag(name = "Passenger Controller")
public interface PassengerAPI {

    @Operation(summary = "Get passenger by Id")
    PassengerResponse getPassengerById(int id);

    @Operation(summary = "Get all passengers for Admin")
    PassengerContainerResponse getAllPassengersAdmin(@Min(0) int offset,
                                                     @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all passengers")
    PassengerContainerResponse getAllPassengers(@Min(0) int offset,
                                                @Min(1) @Max(100) int limit);

    @Operation(summary = "Get passenger by phone")
    PassengerResponse getPassengerByPhone(@Valid @Validated @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "{passengerPhone.pattern}") String phone);

    @Operation(summary = "Get passenger by email")
    PassengerResponse getPassengerByEmail(@Valid @Validated @Pattern(regexp = EMAIL_PATTERN, message = "{passengerEmail.pattern}") String email);

    @Operation(summary = "Soft delete passenger")
    void softDeleteDriverById(int id, JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Hard delete passenger")
    void hardDeleteDriverById(int id);

    @Operation(summary = "Create new passenger")
    ResponseEntity<PassengerResponse> createDriver(@Valid @Validated(OnCreate.class) PassengerRequest request);

    @Operation(summary = "Update passenger by Id")
    PassengerResponse updateDriver(int id, @Valid @Validated(OnUpdate.class) PassengerRequest request, JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Update driver password by id")
    void updatePassword(int id, @Valid @Validated(OnUpdate.class) PasswordRequest request, JwtAuthenticationToken jwtAuthenticationToken);
}
