package cab.aggregator.app.rideservice.client.driver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(name = "${driver-service-wire-mock.name}", url = "${driver-service-wire-mock.url}", path = "${driver-service-wire-mock.path}")
@Profile("test")
public interface DriverClientIntegrationTest extends DriverClient {
}
