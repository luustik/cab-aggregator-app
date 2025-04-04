package cab.aggregator.app.driverservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String KAFKA_BOOTSTRAP_SERVERS = "${spring.kafka.bootstrap-servers}";
    public static final String DRIVER_TOPIC = "driver-topic";
    public static final String GROUP_DRIVER = "driver-group";
    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String TRUSTED_PACKAGES = "cab.aggregator.app.driverservice.dto.kafka";
    public static final String DESERIALIZER_AVG_RATING_TO_DRIVER = "cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse" +
            ":cab.aggregator.app.driverservice.dto.kafka.AvgRatingResponse";
}

