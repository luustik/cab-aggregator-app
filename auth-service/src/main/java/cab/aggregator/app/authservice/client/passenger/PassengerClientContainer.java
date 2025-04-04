package cab.aggregator.app.authservice.client.passenger;

import cab.aggregator.app.authservice.dto.request.SignUpDto;
import cab.aggregator.app.authservice.dto.response.PassengerResponse;
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
    public PassengerResponse createPassenger(SignUpDto dto, String authToken) {
        return passengerClient.createPassenger(dto, authToken);
    }
}
