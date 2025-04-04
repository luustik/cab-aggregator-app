package cab.aggregator.app.ratingservice.client.passenger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(name = "${passenger-service-wire-mock.name}", url = "${passenger-service-wire-mock.url}", path = "${passenger-service-wire-mock.path}")
@Profile("test")
public interface PassengerClientIntegrationTest extends PassengerClient {
}
