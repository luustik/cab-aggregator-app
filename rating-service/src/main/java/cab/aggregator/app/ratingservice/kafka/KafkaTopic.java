package cab.aggregator.app.ratingservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static cab.aggregator.app.ratingservice.kafka.util.Constants.DRIVER_TOPIC;
import static cab.aggregator.app.ratingservice.kafka.util.Constants.PASSENGER_TOPIC;

@Configuration
public class KafkaTopic {

    private final int partitionCount = 3;

    @Bean
    public NewTopic driverAvgRatingTopic() {
        return TopicBuilder.name(DRIVER_TOPIC)
                .partitions(partitionCount)
                .build();
    }

    @Bean
    public NewTopic passengerAvgRatingTopic() {
        return TopicBuilder.name(PASSENGER_TOPIC)
                .partitions(partitionCount)
                .build();
    }
}
