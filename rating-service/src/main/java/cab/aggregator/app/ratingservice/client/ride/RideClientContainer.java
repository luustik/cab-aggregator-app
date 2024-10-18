package cab.aggregator.app.ratingservice.client.ride;

import cab.aggregator.app.ratingservice.dto.client.RideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideClientContainer {

    private final RideClient rideClient;

    public RideResponse getById(long id){
        return rideClient.getRideById(id);
    }
}
