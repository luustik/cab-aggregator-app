package cab.aggregator.app.driverservice.service;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.dto.response.CarContainerResponse;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface CarService {

    CarResponse getCarById(int carId);

    CarContainerResponse getAllCars(int offset, int limit);

    CarResponse getCarByCarNumber(String carNumber);

    CarContainerResponse getAllCarsByDriverId(int driverId, int offset, int limit);

    CarResponse createCar(CarRequest carRequestDto, JwtAuthenticationToken token);

    CarResponse updateCar(int carId, CarRequest carRequestDto, JwtAuthenticationToken token);

    void deleteCar(int carId, JwtAuthenticationToken token);

}
