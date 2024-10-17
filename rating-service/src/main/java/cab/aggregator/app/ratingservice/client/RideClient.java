package cab.aggregator.app.ratingservice.client;

import cab.aggregator.app.ratingservice.dto.client.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ride")
public interface RideClient {

    @GetMapping("/{id}")
    RideResponse getRideById(@PathVariable Long id);
}
