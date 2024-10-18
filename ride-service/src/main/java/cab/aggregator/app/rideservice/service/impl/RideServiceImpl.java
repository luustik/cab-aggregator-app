package cab.aggregator.app.rideservice.service.impl;

import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import cab.aggregator.app.rideservice.entity.Ride;
import cab.aggregator.app.rideservice.entity.enums.Status;
import cab.aggregator.app.rideservice.exception.EntityNotFoundException;
import cab.aggregator.app.rideservice.mapper.RideContainerMapper;
import cab.aggregator.app.rideservice.mapper.RideMapper;
import cab.aggregator.app.rideservice.repository.RideRepository;
import cab.aggregator.app.rideservice.service.RideService;
import cab.aggregator.app.rideservice.utility.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

import static cab.aggregator.app.rideservice.utility.Constants.ENTITY_WITH_ID_NOT_FOUND_MESSAGE;
import static cab.aggregator.app.rideservice.utility.ResourceName.RIDE;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final MessageSource messageSource;
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final RideContainerMapper rideContainerMapper;
    private final CalculationCost calculationCost;
    private final ValidationStatusService validationStatusService;
    private final ValidatorClientService validatorClientService;

    @Override
    @Transactional(readOnly = true)
    public RideResponse getRideById(Long rideId) {
        return rideMapper.toDto(findById(rideId));
    }

    @Override
    @Transactional(readOnly = true)
    public RideContainerResponse getAllRides(int offset, int limit) {
        return rideContainerMapper
                .toContainer(rideRepository
                        .findAll(PageRequest.of(offset, limit))
                        .map(rideMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public RideContainerResponse getAllRidesByDriverId(Long driverId, int offset, int limit) {
        validatorClientService.checkIfExistDriver(driverId);
        return rideContainerMapper
                .toContainer(rideRepository
                        .findAllByDriverId(driverId, PageRequest.of(offset, limit))
                        .map(rideMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public RideContainerResponse getAllRidesByStatus(String status, int offset, int limit) {
        return rideContainerMapper
                .toContainer(rideRepository
                        .findAllByStatus(Status.valueOf(status.toUpperCase()), PageRequest.of(offset, limit))
                        .map(rideMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public RideContainerResponse getAllRidesByPassengerId(Long passengerId, int offset, int limit) {
        validatorClientService.checkIfExistPassenger(passengerId);
        return rideContainerMapper
                .toContainer(rideRepository
                        .findAllByPassengerId(passengerId, PageRequest.of(offset, limit))
                        .map(rideMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public RideContainerResponse getAllBetweenOrderDateTime(String start, String end, int offset, int limit) {
        return rideContainerMapper
                .toContainer(rideRepository
                        .findAllByOrderDateTimeBetween(Utilities.convertStringToLocalDateTime(start),
                                Utilities.convertStringToLocalDateTime(end), PageRequest.of(offset, limit))
                        .map(rideMapper::toDto));
    }

    @Override
    @Transactional
    public void deleteRide(Long id) {
        Ride ride = findById(id);
        rideRepository.delete(ride);
    }

    @Override
    @Transactional
    public RideResponse updateRideStatus(Long id, String status) {
        Ride ride = findById(id);
        Status newStatus = validationStatusService.validateStatus(ride.getStatus(),
                Status.valueOf(status.toUpperCase()));
        ride.setStatus(newStatus);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Transactional
    public RideResponse createRide(RideRequest rideRequest) {
        Ride ride = rideMapper.toEntity(rideRequest);
        validatorClientService.checkIfExistDriver(ride.getDriverId());
        validatorClientService.checkIfExistPassenger(ride.getPassengerId());
        ride.setOrderDateTime(LocalDateTime.now());
        ride.setCost(calculationCost.generatePrice());
        ride.setStatus(Status.CREATED);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Transactional
    public RideResponse updateRide(Long id, RideRequest rideRequest) {
        Ride ride = findById(id);
        validatorClientService.checkIfExistDriver(rideRequest.driverId());
        validatorClientService.checkIfExistPassenger(rideRequest.passengerId());
        rideMapper.updateRideFromDto(rideRequest, ride);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    private Ride findById(Long rideId) {
        return rideRepository
                .findById(rideId)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_MESSAGE,
                        new Object[]{RIDE, rideId}, Locale.getDefault())));
    }
}
