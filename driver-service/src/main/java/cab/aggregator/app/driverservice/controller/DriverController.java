package cab.aggregator.app.driverservice.controller;

import cab.aggregator.app.driverservice.dto.request.DriverRequest;
import cab.aggregator.app.driverservice.dto.request.PasswordRequest;
import cab.aggregator.app.driverservice.dto.response.DriverContainerResponse;
import cab.aggregator.app.driverservice.dto.response.DriverResponse;
import cab.aggregator.app.driverservice.dto.validation.OnCreate;
import cab.aggregator.app.driverservice.dto.validation.OnUpdate;
import cab.aggregator.app.driverservice.service.DriverService;
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

import static cab.aggregator.app.driverservice.utility.Constants.GENDER_PATTERN;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@Validated
public class DriverController implements DriverAPI {

    private final DriverService driverService;

    @Override
    @GetMapping("/{id}")
    public DriverResponse getDriverById(@PathVariable int id) {
        return driverService.getDriverById(id);
    }

    @Override
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public DriverContainerResponse getAllDriversAdmin(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                      @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return driverService.getAllDriversAdmin(offset, limit);
    }

    @Override
    @GetMapping
    public DriverContainerResponse getAllDrivers(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                 @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return driverService.getAllDrivers(offset, limit);
    }

    @Override
    @GetMapping("/driver-by-gender/{gender}")
    public DriverContainerResponse getAllDriversByGender(@Valid @Validated
                                                         @PathVariable
                                                         @Pattern(regexp = GENDER_PATTERN, message = "{gender.pattern}") String gender,
                                                         @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                         @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return driverService.getDriversByGender(gender, offset, limit);
    }

    @Override
    @DeleteMapping("/safe/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public void safeDeleteDriverById(@PathVariable int id, JwtAuthenticationToken jwtAuthenticationToken) {
        driverService.safeDeleteDriver(id, jwtAuthenticationToken);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDriverById(@PathVariable int id) {
        driverService.deleteDriver(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('create-users')")
    public ResponseEntity<DriverResponse> createDriver(@Valid @Validated(OnCreate.class)
                                                       @RequestBody DriverRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(driverService.createDriver(request));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public DriverResponse updateDriver(@PathVariable int id,
                                       @Valid @Validated(OnUpdate.class) @RequestBody DriverRequest request,
                                       JwtAuthenticationToken jwtAuthenticationToken) {
        return driverService.updateDriver(id, request, jwtAuthenticationToken);
    }

    @Override
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public void updatePassword(@PathVariable int id,
                               @Valid @Validated(OnUpdate.class) @RequestBody PasswordRequest request, JwtAuthenticationToken jwtAuthenticationToken) {
        driverService.updatePassword(id, request, jwtAuthenticationToken);
    }
}
