package cab.aggregator.app.driverservice.service.impl;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.dto.response.CarContainerResponse;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import cab.aggregator.app.driverservice.entity.Car;
import cab.aggregator.app.driverservice.entity.Driver;
import cab.aggregator.app.driverservice.mapper.CarContainerResponseMapper;
import cab.aggregator.app.driverservice.mapper.CarMapper;
import cab.aggregator.app.driverservice.repository.CarRepository;
import cab.aggregator.app.driverservice.repository.DriverRepository;
import cab.aggregator.app.driverservice.service.CarService;
import cab.aggregator.app.exception.common.AccessDeniedException;
import cab.aggregator.app.exception.common.EntityNotFoundException;
import cab.aggregator.app.exception.common.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static cab.aggregator.app.driverservice.utility.Constants.CAR;
import static cab.aggregator.app.driverservice.utility.Constants.DRIVER;
import static cab.aggregator.app.driverservice.utility.KeycloakConstants.EMAIL_CLAIM;
import static cab.aggregator.app.driverservice.utility.KeycloakConstants.ROLE_ADMIN;
import static cab.aggregator.app.driverservice.utility.MessageKeys.ACCESS_DENIED_KEY;
import static cab.aggregator.app.driverservice.utility.MessageKeys.ENTITY_NOT_FOUND_KEY;
import static cab.aggregator.app.driverservice.utility.MessageKeys.RESOURCE_ALREADY_EXIST_KEY;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarContainerResponseMapper carContainerResponseMapper;
    private final DriverRepository driverRepository;
    private final MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public CarResponse getCarById(int carId) {
        return carMapper.toDto(findCarById(carId));
    }

    @Transactional(readOnly = true)
    @Override
    public CarContainerResponse getAllCars(int offset, int limit) {
        return carContainerResponseMapper.toContainer(carRepository
                .findAll(PageRequest.of(offset, limit))
                .map(carMapper::toDto));
    }

    @Transactional(readOnly = true)
    @Override
    public CarResponse getCarByCarNumber(String carNumber) {
        return carMapper.toDto(findCarByCarNumber(carNumber));
    }

    @Transactional(readOnly = true)
    @Override
    public CarContainerResponse getAllCarsByDriverId(int driverId, int offset, int limit) {
        return carContainerResponseMapper.toContainer(carRepository
                .findAllByDriverId(driverId, PageRequest.of(offset, limit))
                .map(carMapper::toDto));
    }

    @Override
    @Transactional
    public CarResponse createCar(CarRequest carRequestDto, JwtAuthenticationToken token) {
        checkIfCarUnique(carRequestDto);
        Car car = carMapper.toEntity(carRequestDto);
        car.setDriver(findDriverById(carRequestDto));
        validateCarAccessOrThrow(car, token);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    @Transactional
    public CarResponse updateCar(int carId, CarRequest carRequestDto, JwtAuthenticationToken token) {
        Car car = findCarById(carId);
        if (!car.getCarNumber().equals(carRequestDto.carNumber())) {
            checkIfCarUnique(carRequestDto);
        }
        carMapper.updateCarFromDto(carRequestDto, car);
        car.setDriver(findDriverById(carRequestDto));
        validateCarAccessOrThrow(car, token);
        carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    @Transactional
    public void deleteCar(int carId, JwtAuthenticationToken token) {
        Car car = findCarById(carId);
        validateCarAccessOrThrow(car, token);
        carRepository.delete(car);
    }

    private void validateCarAccessOrThrow(Car car, JwtAuthenticationToken token) {

        if (token.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN))) {
            return;
        }

        String userEmail = token.getToken().getClaims().get(EMAIL_CLAIM).toString();
        if (!car.getDriver().getEmail().equals(userEmail)) {
            throw new AccessDeniedException(
                    messageSource.getMessage(ACCESS_DENIED_KEY,
                            new Object[]{}, LocaleContextHolder.getLocale())
            );
        }
    }

    private void checkIfCarUnique(CarRequest carRequestDto) {
        if (carRepository.existsByCarNumber(carRequestDto.carNumber())) {
            throw new ResourceAlreadyExistsException(messageSource.getMessage(RESOURCE_ALREADY_EXIST_KEY,
                    new Object[]{CAR, carRequestDto.carNumber()}, Locale.getDefault()));
        }
    }

    private Driver findDriverById(CarRequest carRequestDto) {
        return driverRepository.findById(carRequestDto.driverId())
                .filter(driver -> !driver.isDeleted())
                .orElseThrow(() ->
                        new EntityNotFoundException(messageSource.getMessage(ENTITY_NOT_FOUND_KEY,
                                new Object[]{DRIVER, carRequestDto.driverId()}, Locale.getDefault())));
    }

    private Car findCarByCarNumber(String carNumber) {
        return carRepository.findByCarNumber(carNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(messageSource.getMessage(ENTITY_NOT_FOUND_KEY,
                                new Object[]{CAR, carNumber}, Locale.getDefault())));
    }

    private Car findCarById(int carId) {
        return carRepository.findById(carId)
                .orElseThrow(() ->
                        new EntityNotFoundException(messageSource.getMessage(ENTITY_NOT_FOUND_KEY,
                                new Object[]{CAR, carId}, Locale.getDefault())));
    }
}
