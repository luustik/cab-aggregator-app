package cab.aggregator.app.passengerservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String KAFKA_BOOTSTRAP_SERVERS = "${spring.kafka.bootstrap-servers}";
    public static final String PASSENGER_TOPIC = "passenger-topic";
    public static final String GROUP_PASSENGER = "passenger-group";
    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String TRUSTED_PACKAGES = "cab.aggregator.app.passengerservice.dto.kafka";
    public static final String DESERIALIZER_AVG_RATING_TO_PASSENGER = "cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse" +
            ":cab.aggregator.app.passengerservice.dto.kafka.AvgRatingResponse";
}
