package cab.aggregator.app.rideservice.service.impl;

import cab.aggregator.app.rideservice.client.driver.DriverClientContainer;
import cab.aggregator.app.rideservice.client.passenger.PassengerClientContainer;
import cab.aggregator.app.rideservice.dto.client.DriverResponse;
import cab.aggregator.app.rideservice.dto.client.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Validator {

    private final DriverClientContainer driverClientContainer;
    private final PassengerClientContainer passengerClientContainer;

    public void checkIfExistPassenger(long passengerId, String authToken) {
        passengerClientContainer.getById((int) passengerId, authToken);
    }

    public void checkIfExistDriver(long driverId, String authToken) {
        driverClientContainer.getById((int) driverId, authToken);
    }

    public DriverResponse getDriverResponse(long driverId, String authToken) {
        return driverClientContainer.getById((int) driverId, authToken);
    }

    public PassengerResponse getPassengerResponse(long passengerId, String authToken) {
        return passengerClientContainer.getById((int) passengerId, authToken);
    }
}
