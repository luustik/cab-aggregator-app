package cab.aggregator.app.rideservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakConstants {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String AZP_CLAIM = "azp";
    public static final String AZP_CLAIM_VALUE = "admin-cli";
    public static final String REALM_ACCESS_CLAIM =  "realm_access";
    public static final String REALM_ACCESS_CLAIM_VALUE = "roles";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String EMAIL_CLAIM = "email";
}
