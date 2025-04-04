package cab.aggregator.app.rideservice.client.passenger;

import cab.aggregator.app.rideservice.dto.client.PassengerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PassengerClientContainer {

    private final PassengerClient passengerClient;

    @CircuitBreaker(name = "passenger-service-circuit-breaker")
    public PassengerResponse getById(int id, String authorization) {
        return passengerClient.getPassengerById(id, authorization);
    }
}

