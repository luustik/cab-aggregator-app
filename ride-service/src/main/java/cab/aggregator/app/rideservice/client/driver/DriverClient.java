package cab.aggregator.app.rideservice.client.driver;

import cab.aggregator.app.rideservice.dto.client.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Profile("default")
@FeignClient(name = "${feign.client.name.driver}", path = "${feign.client.path.driver}")
public interface DriverClient {

    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable int id, @RequestHeader("Authorization") String authorization);
}
