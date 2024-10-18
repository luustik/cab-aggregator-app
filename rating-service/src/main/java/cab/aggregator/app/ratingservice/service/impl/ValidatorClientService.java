package cab.aggregator.app.ratingservice.service.impl;

import cab.aggregator.app.ratingservice.client.driver.DriverClientContainer;
import cab.aggregator.app.ratingservice.client.passenger.PassengerClientContainer;
import cab.aggregator.app.ratingservice.client.ride.RideClientContainer;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatorClientService {

    private final DriverClientContainer driverClientContainer;
    private final PassengerClientContainer passengerClientContainer;
    private final RideClientContainer rideClientContainer;

    public void checkIfExistUser(Long userId, UserRole role) {
        switch (role){
            case DRIVER -> checkIfExistDriver(userId);
            case PASSENGER -> checkIfExistPassenger(userId);
        }
    }

    public void checkIfExistRide(Long rideId){
        rideClientContainer.getById(rideId);
    }

    private void checkIfExistPassenger(Long passengerId){
        passengerClientContainer.getById(passengerId.intValue());
    }

    private void checkIfExistDriver(Long driverId){
        driverClientContainer.getById(driverId.intValue());
    }
}
