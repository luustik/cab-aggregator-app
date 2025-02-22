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

    public void checkIfExistPassenger(long passengerId, String authorization) {
        passengerClientContainer.getById((int) passengerId, authorization);
    }

    public void checkIfExistDriver(long driverId, String authorization) {
        driverClientContainer.getById((int) driverId, authorization);
    }

    public DriverResponse getDriverResponse(long driverId, String authorization) {
        return driverClientContainer.getById((int) driverId, authorization);
    }

    public PassengerResponse getPassengerResponse(long passengerId, String authorization) {
        return passengerClientContainer.getById((int) passengerId, authorization);
    }
}
