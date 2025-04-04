package cab.aggregator.app.driverservice.kafka;

import cab.aggregator.app.driverservice.dto.kafka.AvgRatingResponse;
import cab.aggregator.app.driverservice.entity.Driver;
import cab.aggregator.app.driverservice.repository.DriverRepository;
import cab.aggregator.app.exception.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static cab.aggregator.app.driverservice.kafka.util.Constants.CONTAINER_FACTORY;
import static cab.aggregator.app.driverservice.kafka.util.Constants.DRIVER_TOPIC;
import static cab.aggregator.app.driverservice.kafka.util.Constants.GROUP_DRIVER;
import static cab.aggregator.app.driverservice.utility.Constants.DRIVER;
import static cab.aggregator.app.driverservice.utility.MessageKeys.ENTITY_NOT_FOUND_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaListenerService {

    private final DriverRepository driverRepository;
    private final MessageSource messageSource;

    @KafkaListener(topics = DRIVER_TOPIC, groupId = GROUP_DRIVER, containerFactory = CONTAINER_FACTORY)
    public void consume(AvgRatingResponse response) {
        Driver driver = findDriverById(response.id());
        driver.setAvgGrade(response.avgRating());
        driverRepository.save(driver);
        log.info("Message from driver topic{}", response);
    }

    private Driver findDriverById(int driverId) {
        return driverRepository.findByIdAndDeletedFalse(driverId).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(ENTITY_NOT_FOUND_KEY,
                        new Object[]{DRIVER, driverId}, LocaleContextHolder.getLocale()))
        );
    }
}
