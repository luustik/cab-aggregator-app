package cab.aggregator.app.driverservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegExp {

    public static final String GENDER_PATTERN = "^(MALE|FEMALE)$";
    public static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[\\w.-]+$";
    public static final String PHONE_NUMBER_PATTERN = "\\+375\\((29|44|33|25)\\)\\d{7}$";
    public static final String CAR_NUMBER_PATTERN = "^\\d{4}[A-Z]{2}-[1-7]$";
}
