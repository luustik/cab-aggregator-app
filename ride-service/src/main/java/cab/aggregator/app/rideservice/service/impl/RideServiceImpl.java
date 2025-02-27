package cab.aggregator.app.rideservice.service.impl;

import cab.aggregator.app.exception.common.AccessDeniedException;
import cab.aggregator.app.exception.common.EntityNotFoundException;
import cab.aggregator.app.rideservice.dto.client.DriverResponse;
import cab.aggregator.app.rideservice.dto.client.PassengerResponse;
import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import cab.aggregator.app.rideservice.entity.Ride;
import cab.aggregator.app.rideservice.entity.enums.Status;
import cab.aggregator.app.rideservice.mapper.RideContainerMapper;
import cab.aggregator.app.rideservice.mapper.RideMapper;
import cab.aggregator.app.rideservice.repository.RideRepository;
import cab.aggregator.app.rideservice.service.RideService;
import cab.aggregator.app.rideservice.utility.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

import static cab.aggregator.app.rideservice.utility.KeycloakConstants.EMAIL_CLAIM;
import static cab.aggregator.app.rideservice.utility.KeycloakConstants.ROLE_ADMIN;
import static cab.aggregator.app.rideservice.utility.MessageKeys.ACCESS_DENIED_KEY;
import static cab.aggregator.app.rideservice.utility.MessageKeys.ENTITY_WITH_ID_NOT_FOUND_KEY;
import static cab.aggregator.app.rideservice.utility.ResourceName.RIDE;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final RideContainerMapper rideContainerMapper;
    private final MessageSource messageSource;
    private final CalculationCost calculationCost;
    private final ValidationStatusService validationStatusService;
    private final Validator validator;

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
        validator.checkIfExistDriver(driverId, getAuthorizationHeader());
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
        validator.checkIfExistPassenger(passengerId, getAuthorizationHeader());
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
    public RideResponse updateRideStatus(Long id, String status, JwtAuthenticationToken token) {
        Ride ride = findById(id);
        validateAccessOrThrow(ride, token);
        Status newStatus = validationStatusService.validateStatus(ride.getStatus(),
                Status.valueOf(status.toUpperCase()));
        ride.setStatus(newStatus);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Transactional
    public RideResponse createRide(RideRequest rideRequest, JwtAuthenticationToken token) {
        Ride ride = rideMapper.toEntity(rideRequest);
        validator.checkIfExistDriver(ride.getDriverId(), getAuthorizationHeader());
        validator.checkIfExistPassenger(ride.getPassengerId(), getAuthorizationHeader());
        validateAccessOrThrow(ride, token);
        ride.setOrderDateTime(LocalDateTime.now());
        ride.setCost(calculationCost.generatePrice());
        ride.setStatus(Status.CREATED);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    private void validateAccessOrThrow(Ride ride, JwtAuthenticationToken token) {
        if (token.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN))) {
            return;
        }

        String userEmail = token.getToken()
                .getClaims()
                .get(EMAIL_CLAIM)
                .toString();

        DriverResponse driverResponse = validator.getDriverResponse(ride.getDriverId(), "Bearer " + token.getToken().getTokenValue());
        PassengerResponse passengerResponse = validator.getPassengerResponse(ride.getPassengerId(), "Bearer " + token.getToken().getTokenValue());

        if (!(driverResponse.email().equals(userEmail)||passengerResponse.email().equals(userEmail))) {
            throw new AccessDeniedException(
                    messageSource.getMessage(ACCESS_DENIED_KEY,
                            new Object[]{}, LocaleContextHolder.getLocale())
            );
        }
    }

    @Override
    @Transactional
    public RideResponse updateRide(Long id, RideRequest rideRequest) {
        Ride ride = findById(id);
        validator.checkIfExistDriver(rideRequest.driverId(), getAuthorizationHeader());
        validator.checkIfExistPassenger(rideRequest.passengerId(), getAuthorizationHeader());
        rideMapper.updateRideFromDto(rideRequest, ride);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    private String getAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        return "Bearer " + token.getToken().getTokenValue();
    }

    private Ride findById(Long rideId) {
        return rideRepository
                .findById(rideId)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_KEY,
                        new Object[]{RIDE, rideId}, Locale.getDefault())));
    }
}
