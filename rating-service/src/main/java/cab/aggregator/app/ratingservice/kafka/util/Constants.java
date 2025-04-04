package cab.aggregator.app.ratingservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String KAFKA_BOOTSTRAP_SERVERS = "${spring.kafka.bootstrap-servers}";
    public static final String DRIVER_TOPIC = "driver-topic";
    public static final String PASSENGER_TOPIC = "passenger-topic";
}
