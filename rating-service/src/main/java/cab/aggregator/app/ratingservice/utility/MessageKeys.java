package cab.aggregator.app.ratingservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageKeys {

    public static final String VALIDATION_FAILED_KEY = "validation.failed.message";
    public static final String DEFAULT_EXCEPTION_KEY = "default.exception.message";
    public static final String ENTITY_WITH_ID_NOT_FOUND_KEY = "entity.not.found.message";
    public static final String ENTITY_RESOURCE_NOT_FOUND_KEY = "entity.resource.not.found.message";
    public static final String RESOURCE_ALREADY_EXISTS_KEY = "resource.already.exists.message";
    public static final String ACCESS_DENIED_KEY = "access.denied.message";
}
