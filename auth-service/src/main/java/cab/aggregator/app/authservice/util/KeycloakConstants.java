package cab.aggregator.app.authservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakConstants {
    public static final String AUTH_TOKEN = "Bearer ";
    public static final String GENDER_FIELD = "gender";

    public static final String GRANT_TYPE_FIELD = "grant_type";
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String CLIENT_ID_FIELD = "client_id";
    public static final String GRANT_TYPE_PASSWORD_FIELD = "password";
    public static final String CLIENT_SECRET_FIELD = "client_secret";
    public static final String REFRESH_TOKEN_FIELD = "refresh_token";
}
