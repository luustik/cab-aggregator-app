package cab.aggregator.app.rideservice.client.driver;

import cab.aggregator.app.rideservice.dto.client.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverClientContainer {

    private final DriverClient driverClient;

    public DriverResponse getById(int id){
        return driverClient.getDriverById(id);
    }
}
