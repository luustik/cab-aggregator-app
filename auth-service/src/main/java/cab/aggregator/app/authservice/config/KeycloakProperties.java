package cab.aggregator.app.authservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Data
public class KeycloakProperties {

    private String realm;
    private UserManagement userManagement;

    @Data
    public static class UserManagement {
        private String clientId;
        private String clientSecret;
        private String serverUrl;
    }

}
