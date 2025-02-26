package cab.aggregator.app.config;

import cab.aggregator.app.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionControllerConfig {

    @Bean
    @ConditionalOnBean(MessageSource.class)
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler exceptionController(MessageSource messageSource) {
        return new GlobalExceptionHandler(messageSource);
    }
}
