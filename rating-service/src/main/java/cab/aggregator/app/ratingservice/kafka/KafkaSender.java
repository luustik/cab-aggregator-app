package cab.aggregator.app.ratingservice.kafka;

import cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static cab.aggregator.app.ratingservice.kafka.util.Constants.DRIVER_TOPIC;
import static cab.aggregator.app.ratingservice.kafka.util.Constants.PASSENGER_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAvgRatingPassenger(AvgRatingUserResponse passengerAvgRating) {
        kafkaTemplate.send(PASSENGER_TOPIC, passengerAvgRating);
        logger(passengerAvgRating);
    }

    public void sendAvgRatingDriver(AvgRatingUserResponse driverAvgRating) {
        kafkaTemplate.send(DRIVER_TOPIC, driverAvgRating);
        logger(driverAvgRating);
    }

    private void logger(AvgRatingUserResponse avgRatingUserResponse) {
        log.info("Send to topic message{}", avgRatingUserResponse);
    }
}
