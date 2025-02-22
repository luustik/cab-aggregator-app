package cab.aggregator.app.driverservice.controller;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.dto.response.CarContainerResponse;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import cab.aggregator.app.driverservice.dto.validation.OnCreate;
import cab.aggregator.app.driverservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Car controller")
public interface CarAPI {
    @Operation(summary = "Get car by Id")
    CarResponse getCarById(int id);

    @Operation(summary = "Get all cars")
    CarContainerResponse getAllCars(@Min(0) int offset,
                                    @Min(1) @Max(100) int limit);

    @Operation(summary = "Get car by car number")
    CarResponse getCarByCarNumber(String carNumber);

    @Operation(summary = "Get all cars by driver id")
    CarContainerResponse getAllCarsByDriverId(int driverId,
                                              @Min(0) int offset,
                                              @Min(1) @Max(100) int limit);

    @Operation(summary = "Delete car by id")
    void deleteCarById(int id, JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Create new car")
    ResponseEntity<CarResponse> createCar(@Valid @Validated(OnCreate.class) CarRequest request, JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Update car by id")
    CarResponse updateCar(int id, @Valid @Validated(OnUpdate.class) CarRequest request, JwtAuthenticationToken jwtAuthenticationToken);
}