package cab.aggregator.app.driverservice.controller;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.dto.response.CarContainerResponse;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import cab.aggregator.app.driverservice.dto.validation.OnCreate;
import cab.aggregator.app.driverservice.dto.validation.OnUpdate;
import cab.aggregator.app.driverservice.service.CarService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
@Validated
public class CarController implements CarAPI {

    private final CarService carService;

    @Override
    @GetMapping("/{id}")
    public CarResponse getCarById(@PathVariable int id) {
        return carService.getCarById(id);
    }

    @Override
    @GetMapping
    public CarContainerResponse getAllCars(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                           @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return carService.getAllCars(offset, limit);
    }

    @Override
    @GetMapping("/car-by-number/{carNumber}")
    public CarResponse getCarByCarNumber(@PathVariable String carNumber) {
        return carService.getCarByCarNumber(carNumber);
    }

    @Override
    @GetMapping("/cars-driver/{driverId}")
    public CarContainerResponse getAllCarsByDriverId(@PathVariable int driverId,
                                                     @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                     @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return carService.getAllCarsByDriverId(driverId, offset, limit);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public void deleteCarById(@PathVariable int id, JwtAuthenticationToken jwtAuthenticationToken) {
        carService.deleteCar(id, jwtAuthenticationToken);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<CarResponse> createCar(@Valid @Validated(OnCreate.class) @RequestBody CarRequest request,
                                                 JwtAuthenticationToken jwtAuthenticationToken) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carService.createCar(request, jwtAuthenticationToken));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public CarResponse updateCar(@PathVariable int id,
                                 @Valid @Validated(OnUpdate.class) @RequestBody CarRequest request,
                                 JwtAuthenticationToken jwtAuthenticationToken) {
        return carService.updateCar(id, request, jwtAuthenticationToken);
    }
}
