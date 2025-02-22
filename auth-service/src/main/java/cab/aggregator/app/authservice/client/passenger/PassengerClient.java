package cab.aggregator.app.authservice.client.passenger;

import cab.aggregator.app.authservice.dto.request.SignUpDto;
import cab.aggregator.app.authservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.client.name.passenger}", path = "${feign.client.path.passenger}")
public interface PassengerClient {

    @PostMapping
    PassengerResponse createPassenger(@RequestBody SignUpDto dto, @RequestHeader("Authorization") String token );
}
