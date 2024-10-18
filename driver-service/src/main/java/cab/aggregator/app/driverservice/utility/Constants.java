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

    public static final String GENDER_PATTERN = "^(MALE|FEMALE)$";
    public static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[\\w.-]+$";
    public static final String PHONE_NUMBER_PATTERN = "\\+375\\((29|44|33|25)\\)\\d{7}$";
    public static final String CAR_NUMBER_PATTERN = "^\\d{4}[A-Z]{2}-[1-7]$";
}
