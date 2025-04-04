package cab.aggregator.app.authservice.client.driver;

import cab.aggregator.app.authservice.dto.request.SignUpDto;
import cab.aggregator.app.authservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.client.name.driver}", path = "${feign.client.path.driver}")
public interface DriverClient {

    @PostMapping
    DriverResponse createDriver(@RequestBody SignUpDto dto, @RequestHeader("Authorization") String token);
}
