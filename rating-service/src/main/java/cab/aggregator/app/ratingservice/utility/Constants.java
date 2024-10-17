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
    public static final String RESOURCE_ALREADY_EXISTS_MESSAGE = "resource.already.exists.message";

    public static final String RATING = "Rating";
    public static final String RIDE = "Ride";
}