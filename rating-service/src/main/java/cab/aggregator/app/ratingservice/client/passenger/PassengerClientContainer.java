package cab.aggregator.app.ratingservice.client.passenger;

import cab.aggregator.app.ratingservice.dto.client.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerClientContainer {

    private final PassengerClient passengerClient;

    public PassengerResponse getById(int id){
        return passengerClient.getPassengerById(id);
    }
}
