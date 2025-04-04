package cab.aggregator.app.ratingservice.client.ride;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(name = "${ride-service-wire-mock.name}", url = "${ride-service-wire-mock.url}", path = "${ride-service-wire-mock.path}")
@Profile("test")
public interface RideClientIntegrationTest extends RideClient {
}
