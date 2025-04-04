package cab.aggregator.app.rideservice.service;

import cab.aggregator.app.rideservice.client.driver.DriverClientContainer;
import cab.aggregator.app.rideservice.client.passenger.PassengerClientContainer;
import cab.aggregator.app.rideservice.exception.ExternalClientException;
import cab.aggregator.app.rideservice.service.impl.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Mock
    private DriverClientContainer driverClientContainer;

    @Mock
    private PassengerClientContainer passengerClientContainer;

    @InjectMocks
    private Validator validator;

    @Test
    void checkIfExistsPassenger_whenPassengerExists_thenSuccess() {
        when(passengerClientContainer.getById(PASSENGER_ID.intValue()))
                .thenReturn(PASSENGER_RESPONSE);

        validator.checkIfExistPassenger(PASSENGER_ID.intValue());

        verify(passengerClientContainer).getById(PASSENGER_ID.intValue());
    }

    @Test
    void checkIfExistsPassenger_whenPassengerNotExists_throwExternalClientException() {
        when(passengerClientContainer.getById(PASSENGER_ID.intValue()))
                .thenThrow(ExternalClientException.class);

        assertThrows(ExternalClientException.class, () -> validator.checkIfExistPassenger(PASSENGER_ID.intValue()));
        verify(passengerClientContainer).getById(PASSENGER_ID.intValue());
    }

    @Test
    void checkIfExistsDriver_whenDriverExists_thenSuccess() {
        when(driverClientContainer.getById(DRIVER_ID.intValue()))
                .thenReturn(DRIVER_RESPONSE);

        validator.checkIfExistDriver(DRIVER_ID.intValue());

        verify(driverClientContainer).getById(DRIVER_ID.intValue());
    }

    @Test
    void checkIfExistsDiver_whenDriverNotExists_throwExternalClientException() {
        when(driverClientContainer.getById(DRIVER_ID.intValue()))
                .thenThrow(ExternalClientException.class);

        assertThrows(ExternalClientException.class, () -> validator.checkIfExistDriver(DRIVER_ID.intValue()));
        verify(driverClientContainer).getById(DRIVER_ID.intValue());
    }
}
