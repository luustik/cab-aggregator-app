package cab.aggregator.app.rideservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegExp {

    public static final String REGEXP_STATUS = "^(?i)(CREATED|ACCEPTED|WAY_TO_PASSENGER|WAY_TO_DESTINATION|COMPLETED|CANCELLED)$";
    public static final String REGEXP_DATE_TIME = "^(20[0-9]{2}|21[0-0])-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$";

}
