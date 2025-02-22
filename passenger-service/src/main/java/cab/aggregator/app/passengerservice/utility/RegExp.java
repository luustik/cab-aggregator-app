package cab.aggregator.app.passengerservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegExp {

    public static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[\\w.-]+$";
    public static final String PHONE_NUMBER_PATTERN = "\\+375\\((29|44|33|25)\\)\\d{7}$";
}
