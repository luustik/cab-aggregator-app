package cab.aggregator.app.rideservice.client.driver;

import cab.aggregator.app.rideservice.dto.client.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.client.name.driver}")
public interface DriverClient {

    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable int id);
}