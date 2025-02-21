package cab.aggregator.app.rideservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String REGEXP_STATUS = "^(?i)(CREATED|ACCEPTED|WAY_TO_PASSENGER|WAY_TO_DESTINATION|COMPLETED|CANCELLED)$";
    public static final String REGEXP_DATE_TIME = "^(20[0-9]{2}|21[0-0])-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$";

    public static final String VALIDATION_FAILED_MESSAGE = "validation.failed.message";
    public static final String VALIDATION_STATUS_FAILED_MESSAGE = "validation.status.failed.message";
    public static final String ENTITY_WITH_ID_NOT_FOUND_MESSAGE = "entity.with.id.not.found.message";
    public static final String DEFAULT_EXCEPTION_MESSAGE = "default.exception.message";
    public static final String ACCESS_DENIED_MESSAGE = "access.denied.message";

    public static final String MESSAGE = "message";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String AZP_CLAIM = "azp";
    public static final String AZP_CLAIM_VALUE = "admin-cli";
    public static final String REALM_ACCESS_CLAIM =  "realm_access";
    public static final String REALM_ACCESS_CLAIM_VALUE = "roles";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String EMAIL_CLAIM = "email";

    public static final String ACTUATOR_ENDPOINT = "/actuator/**";
    public static final String SWAGGER_UI_ENDPOINT = "/swagger-ui/**";
    public static final String API_DOCS_ENDPOINT = "/v3/api-docs/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String WEBJARS_ENDPOINT = "/webjars/**";
}
