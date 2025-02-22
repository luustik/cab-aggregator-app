package cab.aggregator.app.ratingservice.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegExp {

    public static final String REGEXP_ROLE = "^(DRIVER|PASSENGER)$";
}
