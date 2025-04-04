package cab.aggregator.app;

import cab.aggregator.app.logging.HttpLoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Bean
    public HttpLoggingAspect httpLoggingAspect() {
        return new HttpLoggingAspect();
    }
}
