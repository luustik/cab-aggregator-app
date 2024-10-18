package cab.aggregator.app.ratingservice.client.driver;

import cab.aggregator.app.ratingservice.dto.client.DriverResponse;
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
