package cab.aggregator.app.rideservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageKeys {

    public static final String VALIDATION_FAILED_KEY = "validation.failed.message";
    public static final String VALIDATION_STATUS_FAILED_KEY = "validation.status.failed.message";
    public static final String ENTITY_WITH_ID_NOT_FOUND_KEY = "entity.with.id.not.found.message";
    public static final String DEFAULT_EXCEPTION_KEY = "default.exception.message";
    public static final String ACCESS_DENIED_KEY = "access.denied.message";
}
