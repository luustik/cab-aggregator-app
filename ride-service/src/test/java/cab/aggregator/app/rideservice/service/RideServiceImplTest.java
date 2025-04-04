package cab.aggregator.app.rideservice.service;

import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import cab.aggregator.app.rideservice.exception.EntityNotFoundException;
import cab.aggregator.app.rideservice.mapper.RideContainerMapper;
import cab.aggregator.app.rideservice.mapper.RideMapper;
import cab.aggregator.app.rideservice.repository.RideRepository;
import cab.aggregator.app.rideservice.service.impl.CalculationCost;
import cab.aggregator.app.rideservice.service.impl.RideServiceImpl;
import cab.aggregator.app.rideservice.service.impl.ValidationStatusService;
import cab.aggregator.app.rideservice.service.impl.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static cab.aggregator.app.rideservice.entity.enums.Status.CREATED;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.LIMIT;
import static cab.aggregator.app.rideservice.utils.RideConstants.OFFSET;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_CONTAINER_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_COST;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_END_RANGE_TIME;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_END_RANGE_TIME_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_PAGE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_REQUEST;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_RESPONSE_PAGE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_START_RANGE_TIME;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_START_RANGE_TIME_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private RideContainerMapper rideContainerMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CalculationCost calculationCost;

    @Mock
    private ValidationStatusService validationStatusService;

    @Mock
    private Validator validator;

    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void getRideById_whenRideExists_returnRideResponse() {
        when(rideRepository.findById(RIDE_ID))
                .thenReturn(Optional.of(RIDE));
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);

        RideResponse checkedRideResponse = rideService.getRideById(RIDE_ID);

        verify(rideRepository).findById(RIDE_ID);
        verify(rideMapper).toDto(RIDE);
        assertEquals(RIDE_RESPONSE, checkedRideResponse);
    }

    @Test
    void getRideById_whenRideNotExists_throwEntityNotFoundException() {
        when(rideRepository.findById(RIDE_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> rideService.getRideById(RIDE_ID));
        verify(rideRepository).findById(RIDE_ID);
        verify(rideMapper, never()).toDto(RIDE);
    }

    @Test
    void getAllRides_returnRideContainerResponse() {
        when(rideRepository.findAll(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RIDE_PAGE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);
        when(rideContainerMapper.toContainer(RIDE_RESPONSE_PAGE))
                .thenReturn(RIDE_CONTAINER_RESPONSE);

        RideContainerResponse actualRideContainerResponse = rideService.getAllRides(OFFSET, LIMIT);

        verify(rideRepository).findAll(PageRequest.of(OFFSET, LIMIT));
        verify(rideMapper).toDto(RIDE);
        verify(rideContainerMapper).toContainer(RIDE_RESPONSE_PAGE);
        assertEquals(RIDE_CONTAINER_RESPONSE, actualRideContainerResponse);
    }

    @Test
    void getAllRidesByDriverId_returnRideContainerResponse() {
        when(rideRepository.findAllByDriverId(DRIVER_ID, PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RIDE_PAGE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);
        when(rideContainerMapper.toContainer(RIDE_RESPONSE_PAGE))
                .thenReturn(RIDE_CONTAINER_RESPONSE);

        RideContainerResponse actualRideContainerResponse = rideService.getAllRidesByDriverId(DRIVER_ID, OFFSET, LIMIT);

        verify(rideRepository).findAllByDriverId(DRIVER_ID, PageRequest.of(OFFSET, LIMIT));
        verify(rideMapper).toDto(RIDE);
        verify(rideContainerMapper).toContainer(RIDE_RESPONSE_PAGE);
        assertEquals(RIDE_CONTAINER_RESPONSE, actualRideContainerResponse);
    }

    @Test
    void getAllRidesByPassengerId_returnRideContainerResponse() {
        when(rideRepository.findAllByPassengerId(PASSENGER_ID, PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RIDE_PAGE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);
        when(rideContainerMapper.toContainer(RIDE_RESPONSE_PAGE))
                .thenReturn(RIDE_CONTAINER_RESPONSE);

        RideContainerResponse actualRideContainerResponse = rideService.getAllRidesByPassengerId(PASSENGER_ID, OFFSET, LIMIT);

        verify(rideRepository).findAllByPassengerId(PASSENGER_ID, PageRequest.of(OFFSET, LIMIT));
        verify(rideMapper).toDto(RIDE);
        verify(rideContainerMapper).toContainer(RIDE_RESPONSE_PAGE);
        assertEquals(RIDE_CONTAINER_RESPONSE, actualRideContainerResponse);
    }

    @Test
    void getAllRidesByStatus_returnRideContainerResponse() {
        when(rideRepository.findAllByStatus(CREATED, PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RIDE_PAGE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);
        when(rideContainerMapper.toContainer(RIDE_RESPONSE_PAGE))
                .thenReturn(RIDE_CONTAINER_RESPONSE);

        RideContainerResponse actualRideContainerResponse = rideService.getAllRidesByStatus(RIDE_STATUS, OFFSET, LIMIT);

        verify(rideRepository).findAllByStatus(CREATED, PageRequest.of(OFFSET, LIMIT));
        verify(rideMapper).toDto(RIDE);
        verify(rideContainerMapper).toContainer(RIDE_RESPONSE_PAGE);
        assertEquals(RIDE_CONTAINER_RESPONSE, actualRideContainerResponse);
    }

    @Test
    void getAllBetweenOrderDateTime_returnRideContainerResponse() {
        when(rideRepository.findAllByOrderDateTimeBetween(RIDE_START_RANGE_TIME, RIDE_END_RANGE_TIME, PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RIDE_PAGE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);
        when(rideContainerMapper.toContainer(RIDE_RESPONSE_PAGE))
                .thenReturn(RIDE_CONTAINER_RESPONSE);

        RideContainerResponse actualRideContainerResponse = rideService.getAllBetweenOrderDateTime(RIDE_START_RANGE_TIME_STR, RIDE_END_RANGE_TIME_STR, OFFSET, LIMIT);

        verify(rideRepository).findAllByOrderDateTimeBetween(RIDE_START_RANGE_TIME, RIDE_END_RANGE_TIME, PageRequest.of(OFFSET, LIMIT));
        verify(rideMapper).toDto(RIDE);
        verify(rideContainerMapper).toContainer(RIDE_RESPONSE_PAGE);
        assertEquals(RIDE_CONTAINER_RESPONSE, actualRideContainerResponse);
    }

    @Test
    void deleteRide_whenRideExists_thenSuccessDelete() {

        when(rideRepository.findById(RIDE_ID))
                .thenReturn(Optional.of(RIDE));

        rideService.deleteRide(RIDE_ID);

        verify(rideRepository).findById(RIDE_ID);
        verify(rideRepository).delete(RIDE);
    }

    @Test
    void deleteRide_whenRideNotExists_throwEntityNotFoundException() {

        when(rideRepository.findById(RIDE_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> rideService.deleteRide(RIDE_ID));
        verify(rideRepository).findById(RIDE_ID);
        verify(rideRepository, never()).delete(RIDE);
    }

    @Test
    void createRide_returnRideResponse() {

        when(rideMapper.toEntity(RIDE_REQUEST))
                .thenReturn(RIDE);
        when(calculationCost.generatePrice())
                .thenReturn(RIDE_COST);
        when(rideRepository.save(RIDE))
                .thenReturn(RIDE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);

        RideResponse actual = rideService.createRide(RIDE_REQUEST);

        assertEquals(RIDE_RESPONSE, actual);
        verify(rideMapper).toEntity(RIDE_REQUEST);
        verify(validator).checkIfExistDriver(DRIVER_ID);
        verify(validator).checkIfExistPassenger(PASSENGER_ID);
        verify(calculationCost).generatePrice();
        verify(rideRepository).save(RIDE);
        verify(rideMapper).toDto(RIDE);
    }

    @Test
    void updateRide_whenRideExists_returnRideResponse() {
        when(rideRepository.findById(RIDE_ID))
                .thenReturn(Optional.of(RIDE));
        when(rideRepository.save(RIDE))
                .thenReturn(RIDE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);

        RideResponse actual = rideService.updateRide(RIDE_ID, RIDE_REQUEST);

        assertEquals(RIDE_RESPONSE, actual);
        verify(rideRepository).findById(RIDE_ID);
        verify(validator).checkIfExistDriver(DRIVER_ID);
        verify(validator).checkIfExistPassenger(PASSENGER_ID);
        verify(rideMapper).updateRideFromDto(RIDE_REQUEST, RIDE);
        verify(rideRepository).save(RIDE);
        verify(rideMapper).toDto(RIDE);
    }

    @Test
    void updateRide_whenRideNotExists_throwEntityNotFoundException() {
        when(rideRepository.findById(RIDE_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> rideService.updateRide(RIDE_ID, RIDE_REQUEST));
        verify(rideRepository).findById(RIDE_ID);
        verify(validator, never()).checkIfExistDriver(DRIVER_ID);
        verify(validator, never()).checkIfExistPassenger(PASSENGER_ID);
        verify(rideMapper, never()).updateRideFromDto(RIDE_REQUEST, RIDE);
        verify(rideRepository, never()).save(RIDE);
        verify(rideMapper, never()).toDto(RIDE);
    }

    @Test
    void updateRideStatus_whenRideNotExists_throwEntityNotFoundException() {
        when(rideRepository.findById(RIDE_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> rideService.updateRideStatus(RIDE_ID, RIDE_STATUS));
        verify(rideRepository).findById(RIDE_ID);
        verify(validationStatusService, never()).validateStatus(CREATED, CREATED);
        verify(rideRepository, never()).save(RIDE);
        verify(rideMapper, never()).toDto(RIDE);
    }

    @Test
    void updateRideStatus_whenRideExists_returnRideResponse() {
        when(rideRepository.findById(RIDE_ID))
                .thenReturn(Optional.of(RIDE));
        when(validationStatusService.validateStatus(CREATED, CREATED))
                .thenReturn(CREATED);
        when(rideRepository.save(RIDE))
                .thenReturn(RIDE);
        when(rideMapper.toDto(RIDE))
                .thenReturn(RIDE_RESPONSE);

        RideResponse actual = rideService.updateRideStatus(RIDE_ID, RIDE_STATUS);

        assertEquals(RIDE_RESPONSE, actual);
        verify(rideRepository).findById(RIDE_ID);
        verify(validationStatusService).validateStatus(CREATED, CREATED);
        verify(rideRepository).save(RIDE);
        verify(rideMapper).toDto(RIDE);
    }

}
