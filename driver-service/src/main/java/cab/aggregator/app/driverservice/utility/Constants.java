package cab.aggregator.app.driverservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String CAR = "Car";
    public static final String DRIVER = "Driver";

    public static final String ENTITY_NOT_FOUND_MESSAGE = "entity.not.found.message";
    public static final String RESOURCE_ALREADY_EXIST_MESSAGE = "resource.already.exists.message";
    public static final String VALIDATION_FAILED_MESSAGE = "validation.failed.message";
    public static final String DEFAULT_EXCEPTION_MESSAGE = "default.exception.message";
    public static final String ACCESS_DENIED_MESSAGE = "access.denied.message";
    public static final String ENTITY_NOT_FOUND_KEYCLOAK_MESSAGE = "entity.not.found.keycloak.message";

    public static final String GENDER_PATTERN = "^(MALE|FEMALE)$";
    public static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[\\w.-]+$";
    public static final String PHONE_NUMBER_PATTERN = "\\+375\\((29|44|33|25)\\)\\d{7}$";
    public static final String CAR_NUMBER_PATTERN = "^\\d{4}[A-Z]{2}-[1-7]$";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String EMAIL_CLAIM = "email";
    public static final String AZP_CLAIM = "azp";
    public static final String AZP_CLAIM_VALUE = "admin-cli";
    public static final String REALM_ACCESS_CLAIM =  "realm_access";
    public static final String REALM_ACCESS_CLAIM_VALUE = "roles";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String GENDER_FIELD = "gender";

    public static final String ACTUATOR_ENDPOINT = "/actuator/**";
    public static final String SWAGGER_UI_ENDPOINT = "/swagger-ui/**";
    public static final String API_DOCS_ENDPOINT = "/v3/api-docs/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String WEBJARS_ENDPOINT = "/webjars/**";
}
