package cab.aggregator.app.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApi {
    @Bean
    public OpenAPI openAPI(@Value("${openapi.gateway.swagger-url}") String swaggerUrl) {
        return new OpenAPI()
                .servers(List.of(new Server().url(swaggerUrl)))
                .info(new Info()
                        .title("Cab Aggregator")
                        .description("")
                        .version("1.0")
                );
    }
}
