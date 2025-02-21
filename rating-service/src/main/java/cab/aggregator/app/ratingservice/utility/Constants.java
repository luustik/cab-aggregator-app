package cab.aggregator.app.ratingservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String REGEXP_ROLE = "^(DRIVER|PASSENGER)$";

    public static final String VALIDATION_FAILED_MESSAGE = "validation.failed.message";
    public static final String DEFAULT_EXCEPTION_MESSAGE = "default.exception.message";
    public static final String ENTITY_WITH_ID_NOT_FOUND_MESSAGE = "entity.not.found.message";
    public static final String ENTITY_RESOURCE_NOT_FOUND_MESSAGE = "entity.resource.not.found.message";
    public static final String LIST_EMPTY_MESSAGE = "list.empty.message";
    public static final String RESOURCE_ALREADY_EXISTS_MESSAGE = "resource.already.exists.message";
    public static final String ACCESS_DENIED_MESSAGE = "access.denied.message";

    public static final String RATING = "Rating";
    public static final String RIDE = "Ride";
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