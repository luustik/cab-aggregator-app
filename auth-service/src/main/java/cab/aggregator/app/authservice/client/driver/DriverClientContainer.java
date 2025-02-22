package cab.aggregator.app.authservice.client.driver;

import cab.aggregator.app.authservice.dto.request.SignUpDto;
import cab.aggregator.app.authservice.dto.response.DriverResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverClientContainer {

    private final DriverClient driverClient;

    @CircuitBreaker(name = "driver-service-circuit-breaker")
    public DriverResponse createDriver(SignUpDto dto, String authToken) {
        return driverClient.createDriver(dto, authToken);
    }
}
