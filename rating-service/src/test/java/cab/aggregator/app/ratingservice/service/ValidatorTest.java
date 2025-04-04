package cab.aggregator.app.ratingservice.service;

import cab.aggregator.app.ratingservice.client.driver.DriverClientContainer;
import cab.aggregator.app.ratingservice.client.passenger.PassengerClientContainer;
import cab.aggregator.app.ratingservice.client.ride.RideClientContainer;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import cab.aggregator.app.ratingservice.exception.ExternalClientException;
import cab.aggregator.app.ratingservice.exception.ResourceAlreadyExistException;
import cab.aggregator.app.ratingservice.repository.RatingRepository;
import cab.aggregator.app.ratingservice.service.impl.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVER_ROLE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.PASSENGER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_RESOURCE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RESOURCE_ALREADY_EXIST;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_RESOURCE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.USER_ID;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Mock
    private DriverClientContainer driverClientContainer;

    @Mock
    private PassengerClientContainer passengerClientContainer;

    @Mock
    private RideClientContainer rideClientContainer;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private Validator validator;

    @Test
    void checkIfExistUser_whenUserIsDriverAndUserExists_thenSuccess() {
        when(driverClientContainer.getById(USER_ID.intValue())).thenReturn(DRIVER_RESPONSE);

        validator.checkIfExistUser(USER_ID, UserRole.DRIVER);

        verify(driverClientContainer).getById(USER_ID.intValue());
    }

    @Test
    void checkIfExistUser_whenUserIsDriverAndUserNotExists_throwExternalClientException() {
        when(driverClientContainer.getById(USER_ID.intValue())).thenThrow(ExternalClientException.class);

        assertThrows(ExternalClientException.class, () -> validator.checkIfExistUser(USER_ID, UserRole.DRIVER));
        verify(driverClientContainer).getById(USER_ID.intValue());
    }

    @Test
    void checkIfExistUser_whenUserIsPassengerAndUserExists_thenSuccess() {
        when(passengerClientContainer.getById(USER_ID.intValue())).thenReturn(PASSENGER_RESPONSE);

        validator.checkIfExistUser(USER_ID, UserRole.PASSENGER);

        verify(passengerClientContainer).getById(USER_ID.intValue());
    }

    @Test
    void checkIfExistUser_whenUserIsPassengerAndUserNotExists_throwExternalClientException() {
        when(passengerClientContainer.getById(USER_ID.intValue())).thenThrow(ExternalClientException.class);

        assertThrows(ExternalClientException.class, () -> validator.checkIfExistUser(USER_ID, UserRole.PASSENGER));
        verify(passengerClientContainer).getById(USER_ID.intValue());
    }

    @Test
    void checkIfExistRide_whenRideExists_thenSuccess() {
        when(rideClientContainer.getById(RIDE_ID)).thenReturn(RIDE_RESPONSE);

        validator.checkIfExistRide(RIDE_ID);

        verify(rideClientContainer).getById(RIDE_ID);
    }

    @Test
    void checkIfExistRide_whenRideNotExists_throwExternalClientException() {
        when(rideClientContainer.getById(RIDE_ID)).thenThrow(ExternalClientException.class);

        assertThrows(ExternalClientException.class, () -> validator.checkIfExistRide(RIDE_ID));
        verify(rideClientContainer).getById(RIDE_ID);
    }

    @Test
    void checkIfExistRatingByRideIdAndRole_whenRatingNotExist_thenSuccess() {
        when(ratingRepository.existsByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER))
                .thenReturn(FALSE);

        validator.checkIfExistRatingByRideIdAndRole(RIDE_ID, UserRole.DRIVER);

        verify(ratingRepository).existsByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER);
    }

    @Test
    void checkIfExistRatingByRideIdAndRole_whenRatingExist_throwResourceAlreadyExistException() {
        when(ratingRepository.existsByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXIST, RATING_RESOURCE, RIDE_RESOURCE, RIDE_ID, DRIVER_ROLE));

        ResourceAlreadyExistException ex = assertThrows(ResourceAlreadyExistException.class,
                () -> validator.checkIfExistRatingByRideIdAndRole(RIDE_ID, UserRole.DRIVER));
        assertEquals(String.format(RESOURCE_ALREADY_EXIST, RATING_RESOURCE, RIDE_RESOURCE, RIDE_ID, DRIVER_ROLE), ex.getMessage());
        verify(ratingRepository).existsByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER);
    }
}
