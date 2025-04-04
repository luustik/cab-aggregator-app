package cab.aggregator.app.driverservice.service;

import cab.aggregator.app.driverservice.dto.response.CarContainerResponse;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import cab.aggregator.app.driverservice.exception.EntityNotFoundException;
import cab.aggregator.app.driverservice.exception.ResourceAlreadyExistsException;
import cab.aggregator.app.driverservice.mapper.CarContainerResponseMapper;
import cab.aggregator.app.driverservice.mapper.CarMapper;
import cab.aggregator.app.driverservice.repository.CarRepository;
import cab.aggregator.app.driverservice.repository.DriverRepository;
import cab.aggregator.app.driverservice.service.impl.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;

import java.util.Locale;
import java.util.Optional;

import static cab.aggregator.app.driverservice.utils.CarConstants.CAR;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_CONTAINER_RESPONSE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_ID;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_NUMBER;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_PAGE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_REQUEST;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_RESOURCE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_RESPONSE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_RESPONSE_PAGE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_UPDATED_REQUEST;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_ID;
import static cab.aggregator.app.driverservice.utils.DriverConstants.LIMIT;
import static cab.aggregator.app.driverservice.utils.DriverConstants.OFFSET;
import static cab.aggregator.app.driverservice.utils.DriverConstants.RESOURCE_ALREADY_EXISTS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @Mock
    private CarContainerResponseMapper carContainerResponseMapper;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void getAllCars_returnCarContainerResponse() {
        when(carRepository.findAll(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(CAR_PAGE);
        when(carMapper.toDto(CAR))
                .thenReturn(CAR_RESPONSE);
        when(carContainerResponseMapper.toContainer(CAR_RESPONSE_PAGE))
                .thenReturn(CAR_CONTAINER_RESPONSE);

        CarContainerResponse actualCarContainerResponse = carService.getAllCars(OFFSET, LIMIT);

        verify(carRepository).findAll(PageRequest.of(OFFSET, LIMIT));
        verify(carMapper).toDto(CAR);
        verify(carContainerResponseMapper).toContainer(CAR_RESPONSE_PAGE);
        assertEquals(CAR_CONTAINER_RESPONSE, actualCarContainerResponse);
    }

    @Test
    void getAllCarsByDriverId_returnCarContainerResponse() {
        when(carRepository.findAllByDriverId(DRIVER_ID, PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(CAR_PAGE);
        when(carMapper.toDto(CAR))
                .thenReturn(CAR_RESPONSE);
        when(carContainerResponseMapper.toContainer(CAR_RESPONSE_PAGE))
                .thenReturn(CAR_CONTAINER_RESPONSE);

        CarContainerResponse actualCarContainerResponse = carService.getAllCarsByDriverId(DRIVER_ID, OFFSET, LIMIT);

        verify(carRepository).findAllByDriverId(DRIVER_ID, PageRequest.of(OFFSET, LIMIT));
        verify(carMapper).toDto(CAR);
        verify(carContainerResponseMapper).toContainer(CAR_RESPONSE_PAGE);
        assertEquals(CAR_CONTAINER_RESPONSE, actualCarContainerResponse);
        assertEquals(CAR_CONTAINER_RESPONSE, actualCarContainerResponse);
    }

    @Test
    void getCarById_whenCarExists_returnCarResponse() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(CAR));
        when(carMapper.toDto(CAR))
                .thenReturn(CAR_RESPONSE);

        CarResponse checkedCarResponse = carService.getCarById(CAR_ID);

        verify(carRepository).findById(CAR_ID);
        verify(carMapper).toDto(CAR);
        assertEquals(CAR_RESPONSE, checkedCarResponse);
    }

    @Test
    void getCarById_whenCarNotExists_throwEntityNotFoundException() {
        when(carRepository.findById(CAR_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> carService.getCarById(CAR_ID));
        verify(carRepository).findById(CAR_ID);
    }

    @Test
    void getCarByCarNumber_whenCarExists_returnCarResponse() {
        when(carRepository.findByCarNumber(CAR_NUMBER))
                .thenReturn(Optional.of(CAR));
        when(carMapper.toDto(CAR))
                .thenReturn(CAR_RESPONSE);

        CarResponse checkedCarResponse = carService.getCarByCarNumber(CAR_NUMBER);

        verify(carMapper).toDto(CAR);
        verify(carRepository).findByCarNumber(CAR_NUMBER);
        assertEquals(CAR_RESPONSE, checkedCarResponse);
    }

    @Test
    void getCarByCarNumber_whenCarNotExists_throwEntityNotFoundException() {
        when(carRepository.findByCarNumber(CAR_NUMBER))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> carService.getCarByCarNumber(CAR_NUMBER));
        verify(carRepository).findByCarNumber(CAR_NUMBER);
    }

    @Test
    void createCar_whenNewCarNotExistsAndExistsDriver_returnCarResponse() {
        when(carRepository.existsByCarNumber(CAR_NUMBER))
                .thenReturn(FALSE);
        when(carMapper.toEntity(CAR_REQUEST))
                .thenReturn(CAR);
        when(driverRepository.findById(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(carRepository.save(CAR))
                .thenReturn(CAR);
        when(carMapper.toDto(CAR))
                .thenReturn(CAR_RESPONSE);

        CarResponse actualCarResponse = carService.createCar(CAR_REQUEST);

        assertEquals(CAR_RESPONSE, actualCarResponse);
        verify(carRepository).existsByCarNumber(CAR_NUMBER);
        verify(carMapper).toEntity(CAR_REQUEST);
        verify(driverRepository).findById(DRIVER_ID);
        verify(carRepository).save(CAR);
        verify(carMapper).toDto(CAR);
    }

    @Test
    void createCar_whenNewCarAlreadyExists_throwResourceAlreadyExistsException() {
        when(carRepository.existsByCarNumber(CAR_NUMBER))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, CAR_RESOURCE, CAR_NUMBER));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> carService.createCar(CAR_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, CAR_RESOURCE, CAR_NUMBER), ex.getMessage());
        verify(carRepository).existsByCarNumber(CAR_NUMBER);
        verify(carRepository, never()).findByCarNumber(CAR_NUMBER);
    }

    @Test
    void createCar_whenNewCarNotExistsAndNotExistsDriver_throwEntityNotFoundException() {
        when(carRepository.existsByCarNumber(CAR_NUMBER))
                .thenReturn(FALSE);
        when(carMapper.toEntity(CAR_REQUEST))
                .thenReturn(CAR);
        when(driverRepository.findById(DRIVER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> carService.createCar(CAR_REQUEST));
        verify(carRepository).existsByCarNumber(CAR_NUMBER);
        verify(carMapper).toEntity(CAR_REQUEST);
        verify(driverRepository).findById(DRIVER_ID);
        verify(carRepository, never()).save(CAR);
        verify(carMapper, never()).toDto(CAR);
    }

    @Test
    void deleteCar_whenCarExists_thenSuccessDelete() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(CAR));

        carService.deleteCar(CAR_ID);
        verify(carRepository).delete(CAR);
    }

    @Test
    void deleteCar_whenCarNotExists_throwEntityNotFoundException() {
        when(carRepository.findById(CAR_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> carService.deleteCar(CAR_ID));
        verify(carRepository, never()).delete(CAR);
    }

    @Test
    void updateCar_whenCarIdFoundAndCurrentCarIsFutureCarAndDriverFound_returnCarResponse() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(CAR));
        when(driverRepository.findById(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(carRepository.save(CAR))
                .thenReturn(CAR);
        when(carMapper.toDto(CAR))
                .thenReturn(CAR_RESPONSE);

        CarResponse actualCarResponse = carService.updateCar(CAR_ID, CAR_REQUEST);

        assertEquals(CAR_RESPONSE, actualCarResponse);
        verify(carRepository).findById(CAR_ID);
        verify(carRepository, never()).existsByCarNumber(CAR_NUMBER);
        verify(driverRepository).findById(DRIVER_ID);
        verify(carMapper).updateCarFromDto(CAR_REQUEST, CAR);
        verify(carRepository).save(CAR);
        verify(carMapper).toDto(CAR);
    }

    @Test
    void updateCar_whenCarIdFoundAndCurrentCarIsFutureCarAndDriverNotFound_throwEntityNotFoundException() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(CAR));
        when(driverRepository.findById(DRIVER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> carService.updateCar(CAR_ID, CAR_REQUEST));
        verify(carRepository).findById(CAR_ID);
        verify(carMapper).updateCarFromDto(CAR_REQUEST, CAR);
        verify(driverRepository).findById(DRIVER_ID);
        verify(carRepository, never()).existsByCarNumber(CAR_NUMBER);
        verify(carRepository, never()).save(CAR);
        verify(carMapper, never()).toDto(CAR);
    }

    @Test
    void updateCar_whenCarIdFoundAndCurrentCarIsNotFutureCarAndUpdatedCarNotExistsAndUpdatedDriverFound_returnCarResponse() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(CAR));
        when(carRepository.existsByCarNumber(CAR_UPDATED_REQUEST.carNumber()))
                .thenReturn(FALSE);
        when(driverRepository.findById(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(carRepository.save(CAR))
                .thenReturn(CAR);
        when(carMapper.toDto(CAR))
                .thenReturn(CAR_RESPONSE);

        CarResponse actualCarResponse = carService.updateCar(CAR_ID, CAR_UPDATED_REQUEST);

        assertEquals(CAR_RESPONSE, actualCarResponse);
        verify(carRepository).findById(CAR_ID);
        verify(carRepository).existsByCarNumber(CAR_UPDATED_REQUEST.carNumber());
        verify(driverRepository).findById(DRIVER_ID);
        verify(carMapper).updateCarFromDto(CAR_UPDATED_REQUEST, CAR);
        verify(carRepository).save(CAR);
        verify(carMapper).toDto(CAR);
    }

    @Test
    void updateCar_whenCarIdFoundAndCurrentCarIsNotFutureCarAndUpdatedCarNotExistsAndUpdatedDriverNotExists_throwEntityNotFoundException() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(CAR));
        when(carRepository.existsByCarNumber(CAR_UPDATED_REQUEST.carNumber()))
                .thenReturn(FALSE);
        when(driverRepository.findById(DRIVER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> carService.updateCar(CAR_ID, CAR_UPDATED_REQUEST));
        verify(carRepository).findById(CAR_ID);
        verify(carRepository).existsByCarNumber(CAR_UPDATED_REQUEST.carNumber());
        verify(driverRepository).findById(DRIVER_ID);
        verify(carMapper).updateCarFromDto(CAR_UPDATED_REQUEST, CAR);
        verify(carRepository, never()).save(CAR);
        verify(carMapper, never()).toDto(CAR);
    }

    @Test
    void updateCar_whenCarIdFoundAndCurrentCarIsNotFutureCarAndUpdatedCarExists_throwResourceAlreadyExistsException() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(CAR));
        when(carRepository.existsByCarNumber(CAR_UPDATED_REQUEST.carNumber()))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, CAR_RESOURCE, CAR_UPDATED_REQUEST.carNumber()));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> carService.updateCar(CAR_ID, CAR_UPDATED_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, CAR_RESOURCE, CAR_UPDATED_REQUEST.carNumber()), ex.getMessage());
        verify(carRepository).findById(CAR_ID);
        verify(carRepository).existsByCarNumber(CAR_UPDATED_REQUEST.carNumber());
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(carMapper, never()).updateCarFromDto(CAR_UPDATED_REQUEST, CAR);
        verify(driverRepository, never()).findById(DRIVER_ID);
        verify(carRepository, never()).save(CAR);
        verify(carMapper, never()).toDto(CAR);
    }

    @Test
    void updateCar_whenCarIdNotFound_throwEntityNotFoundException() {
        when(carRepository.findById(CAR_ID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> carService.updateCar(CAR_ID, CAR_REQUEST));
        verify(carRepository).findById(CAR_ID);
        verify(carRepository, never()).existsByCarNumber(CAR_REQUEST.carNumber());
        verify(carMapper, never()).updateCarFromDto(CAR_REQUEST, CAR);
        verify(driverRepository, never()).findById(DRIVER_ID);
        verify(carRepository, never()).save(CAR);
        verify(carMapper, never()).toDto(CAR);
    }
}
