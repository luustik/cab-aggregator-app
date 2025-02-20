package cab.aggregator.app.passengerservice.controller;

import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.dto.response.PassengerContainerResponse;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import cab.aggregator.app.passengerservice.dto.validation.OnCreate;
import cab.aggregator.app.passengerservice.dto.validation.OnUpdate;
import cab.aggregator.app.passengerservice.service.PassengerService;
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
import org.springframework.web.bind.annotation.RequestBody;

import static cab.aggregator.app.passengerservice.utility.Constants.EMAIL_PATTERN;
import static cab.aggregator.app.passengerservice.utility.Constants.PHONE_NUMBER_PATTERN;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
@Validated
public class PassengerControllerImpl implements PassengerAPI {

    private final PassengerService passengerService;

    @Override
    @GetMapping("/{id}")
    public PassengerResponse getPassengerById(@PathVariable int id) {
        return passengerService.getPassengerById(id);
    }

    @Override
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public PassengerContainerResponse getAllPassengersAdmin(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                            @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return passengerService.getAllPassengersAdmin(offset, limit);
    }

    @Override
    @GetMapping
    public PassengerContainerResponse getAllPassengers(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                       @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return passengerService.getAllPassengers(offset, limit);
    }

    @Override
    @GetMapping("/phone/{phone}")
    public PassengerResponse getPassengerByPhone(@Valid
                                                 @Validated
                                                 @PathVariable
                                                 @Pattern(regexp = PHONE_NUMBER_PATTERN,
                                                         message = "{passengerPhone.pattern}") String phone) {
        return passengerService.getPassengerByPhone(phone);
    }

    @Override
    @GetMapping("/email/{email}")
    public PassengerResponse getPassengerByEmail(@Valid
                                                 @Validated
                                                 @PathVariable
                                                 @Pattern(regexp = EMAIL_PATTERN,
                                                         message = "{passengerEmail.pattern}") String email) {
        return passengerService.getPassengerByEmail(email);
    }

    @Override
    @DeleteMapping("/soft/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public void softDeleteDriverById(@PathVariable int id, JwtAuthenticationToken jwtAuthenticationToken) {
        passengerService.softDeletePassenger(id, jwtAuthenticationToken);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void hardDeleteDriverById(@PathVariable int id) {
        passengerService.hardDeletePassenger(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('create-users')")
    public ResponseEntity<PassengerResponse> createDriver(@Valid @Validated(OnCreate.class)
                                                          @RequestBody PassengerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(passengerService.createPassenger(request));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public PassengerResponse updateDriver(@PathVariable int id,
                                          @Valid @Validated(OnUpdate.class) @RequestBody PassengerRequest request,
                                          JwtAuthenticationToken jwtAuthenticationToken) {
        return passengerService.updatePassenger(id, request, jwtAuthenticationToken);
    }
}
