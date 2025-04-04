package cab.aggregator.app.ratingservice.client.ride;

import cab.aggregator.app.ratingservice.dto.client.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Profile("default")
@FeignClient(name = "${feign.client.name.ride}", path = "${feign.client.path.ride}")
public interface RideClient {

    @GetMapping("/{id}")
    RideResponse getRideById(@PathVariable Long id, @RequestHeader("Authorization") String authorization);
}
