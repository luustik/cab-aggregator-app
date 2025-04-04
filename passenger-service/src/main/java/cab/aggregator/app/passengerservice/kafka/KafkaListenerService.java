package cab.aggregator.app.passengerservice.kafka;

import cab.aggregator.app.exception.common.EntityNotFoundException;
import cab.aggregator.app.passengerservice.dto.kafka.AvgRatingResponse;
import cab.aggregator.app.passengerservice.entity.Passenger;
import cab.aggregator.app.passengerservice.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static cab.aggregator.app.passengerservice.kafka.util.Constants.CONTAINER_FACTORY;
import static cab.aggregator.app.passengerservice.kafka.util.Constants.GROUP_PASSENGER;
import static cab.aggregator.app.passengerservice.kafka.util.Constants.PASSENGER_TOPIC;
import static cab.aggregator.app.passengerservice.utility.Constants.PASSENGER;
import static cab.aggregator.app.passengerservice.utility.MessageKeys.ENTITY_WITH_ID_NOT_FOUND_KEY;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaListenerService {

    private final PassengerRepository passengerRepository;
    private final MessageSource messageSource;

    @KafkaListener(topics = PASSENGER_TOPIC, groupId = GROUP_PASSENGER, containerFactory = CONTAINER_FACTORY)
    public void consume(AvgRatingResponse response) {
        Passenger passenger = findPassengerById(response.id());
        passenger.setAvgGrade(response.avgRating());
        passengerRepository.save(passenger);
        log.info("Message from passenger topic{}", response);
    }

    private Passenger findPassengerById(int id) {
        return passengerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_KEY,
                                new Object[]{PASSENGER, id}, LocaleContextHolder.getLocale())));
    }
}
