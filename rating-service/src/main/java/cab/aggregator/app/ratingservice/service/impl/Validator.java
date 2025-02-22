package cab.aggregator.app.ratingservice.service.impl;

import cab.aggregator.app.ratingservice.client.driver.DriverClientContainer;
import cab.aggregator.app.ratingservice.client.passenger.PassengerClientContainer;
import cab.aggregator.app.ratingservice.client.ride.RideClientContainer;
import cab.aggregator.app.ratingservice.dto.client.DriverResponse;
import cab.aggregator.app.ratingservice.dto.client.PassengerResponse;
import cab.aggregator.app.ratingservice.dto.client.RideResponse;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import cab.aggregator.app.ratingservice.exception.ResourceAlreadyExistException;
import cab.aggregator.app.ratingservice.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import static cab.aggregator.app.ratingservice.utility.Constants.RATING;
import static cab.aggregator.app.ratingservice.utility.Constants.RIDE;
import static cab.aggregator.app.ratingservice.utility.MessageKeys.RESOURCE_ALREADY_EXISTS_KEY;

@Component
@RequiredArgsConstructor
public class Validator {

    private final DriverClientContainer driverClientContainer;
    private final PassengerClientContainer passengerClientContainer;
    private final RideClientContainer rideClientContainer;
    private final RatingRepository ratingRepository;
    private final MessageSource messageSource;

    public void checkIfExistUser(Long userId, UserRole role, String authorization) {
        switch (role) {
            case DRIVER -> checkIfExistDriver(userId, authorization);
            case PASSENGER -> checkIfExistPassenger(userId, authorization);
        }
    }

    public void checkIfExistRide(Long rideId, String authorization) {
        rideClientContainer.getById(rideId, authorization);
    }

    private void checkIfExistPassenger(Long passengerId, String authorization) {
        passengerClientContainer.getById(passengerId.intValue(), authorization);
    }

    private void checkIfExistDriver(Long driverId, String authorization) {
        driverClientContainer.getById(driverId.intValue(), authorization);
    }

    public void checkIfExistRatingByRideIdAndRole(Long rideId, UserRole role) {
        if (ratingRepository.existsByRideIdAndUserRole(rideId, role)) {
            throw new ResourceAlreadyExistException(messageSource.getMessage(RESOURCE_ALREADY_EXISTS_KEY,
                    new Object[]{RATING, RIDE, rideId, role.toString()}, LocaleContextHolder.getLocale()));
        }
    }

    public RideResponse getRideByRideId(Long rideId, String authorization) {
        return rideClientContainer.getById(rideId, authorization);
    }

    public DriverResponse getDriverResponse(Long driverId, String authorization) {
        return driverClientContainer.getById(driverId.intValue(), authorization);
    }

    public PassengerResponse getPassengerResponse(Long passengerId, String authorization) {
        return passengerClientContainer.getById(passengerId.intValue(), authorization);
    }

}
