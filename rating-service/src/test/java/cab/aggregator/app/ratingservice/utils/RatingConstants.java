package cab.aggregator.app.ratingservice.utils;

import cab.aggregator.app.ratingservice.dto.client.DriverResponse;
import cab.aggregator.app.ratingservice.dto.client.PassengerResponse;
import cab.aggregator.app.ratingservice.dto.client.RideResponse;
import cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse;
import cab.aggregator.app.ratingservice.dto.request.RatingRequest;
import cab.aggregator.app.ratingservice.dto.request.RatingUpdateDto;
import cab.aggregator.app.ratingservice.dto.response.RatingContainerResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import cab.aggregator.app.ratingservice.entity.Rating;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static cab.aggregator.app.ratingservice.entity.enums.UserRole.DRIVER;
import static cab.aggregator.app.ratingservice.entity.enums.UserRole.PASSENGER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RatingConstants {

    public static final int COUNT_CALLS_METHOD = 1;
    public static final int OFFSET = 0;
    public static final int LIMIT = 20;
    public static final int PAGE_SIZE = 1;
    public static final int RIDE_WIRE_MOCK_SERVER_PORT = 9011;
    public static final int PASSENGER_WIRE_MOCK_SERVER_PORT = 9021;
    public static final int DRIVER_WIRE_MOCK_SERVER_PORT = 9031;

    public static final String LIST_EMPTY = "The %s list is empty";
    public static final String RESOURCE_ALREADY_EXIST = "The %s with %s id %s for %s already exist";
    public static final String RATING_RESOURCE = "Rating";
    public static final String RIDE_RESOURCE = "Ride";

    public static final RideResponse RIDE_RESPONSE = new RideResponse(1, 1, 1, "dw", "afd", "CANCELLED", LocalDateTime.now(), new BigDecimal(500));
    public static final DriverResponse DRIVER_RESPONSE = new DriverResponse(1, "Kirill", "skd@l.wk", "+375(29)1478523", "MALE", false);
    public static final PassengerResponse PASSENGER_RESPONSE = new PassengerResponse(1, "Kirill", "wkn@kn.nv", "+375(44)3698521", false);

    public static final Long RATING_ID = 1L;
    public static final Long RIDE_ID = 1L;
    public static final Long USER_ID = 1L;
    public static final Long VALID_NOT_EXISTS_ID = 10L;
    public static final String DRIVER_ROLE = "DRIVER";
    public static final String PASSENGER_ROLE = "PASSENGER";

    public static final Rating RATING_DRIVER = createRating(DRIVER);
    public static final Rating RATING_PASSENGER = createRating(PASSENGER);

    public static final RatingResponse RATING_DRIVER_RESPONSE = createRatingResponse(DRIVER_ROLE);
    public static final RatingResponse RATING_PASSENGER_RESPONSE = createRatingResponse(PASSENGER_ROLE);
    public static final RatingRequest RATING_DRIVER_REQUEST = new RatingRequest(RIDE_ID, USER_ID, 10, "Good", RatingConstants.DRIVER_ROLE);

    public static final RatingUpdateDto RATING_UPDATE_DTO = new RatingUpdateDto(10, "Good");
    public static final RatingResponse RATING_DRIVER_UPDATED_RESPONSE = new RatingResponse(RATING_ID, RIDE_ID, USER_ID, 2, "Bad", DRIVER_ROLE);

    public static final AvgRatingUserResponse AVG_RATING_USER_RESPONSE = new AvgRatingUserResponse(USER_ID.intValue(), 10.0);

    public static final List<Rating> RATING_LIST = List.of(RATING_PASSENGER, RATING_DRIVER);
    public static final List<Rating> RATING_DRIVER_LIST = List.of(RATING_DRIVER);
    public static final List<RatingResponse> RATING_RESPONSE_LIST = List.of(RATING_PASSENGER_RESPONSE, RATING_DRIVER_RESPONSE);
    public static final Page<Rating> RATING_PAGE = new PageImpl<>(RATING_LIST, PageRequest.of(OFFSET, LIMIT), RATING_LIST.size());
    public static final Page<RatingResponse> RATING_RESPONSE_PAGE = new PageImpl<>(RATING_RESPONSE_LIST, PageRequest.of(OFFSET, LIMIT), RATING_RESPONSE_LIST.size());
    public static final Page<Rating> RATING_DRIVER_PAGE = new PageImpl<>(List.of(RATING_DRIVER), PageRequest.of(OFFSET, LIMIT), PAGE_SIZE);
    public static final Page<RatingResponse> RATING_DRIVER_RESPONSE_PAGE = new PageImpl<>(List.of(RATING_DRIVER_RESPONSE), PageRequest.of(OFFSET, LIMIT), PAGE_SIZE);
    public static final RatingContainerResponse RATING_DRIVER_CONTAINER_RESPONSE = createRatingContainerResponse(RATING_DRIVER_RESPONSE_PAGE);
    public static final RatingContainerResponse RATING_CONTAINER_RESPONSE = createRatingContainerResponse(RATING_RESPONSE_PAGE);

    public static final String LOCALHOST_URL = "http://localhost:8888";
    public static final String RATINGS_ID_URL = "/api/v1/ratings/{id}";
    public static final String RATINGS_URL = "/api/v1/ratings";
    public static final String RATINGS_RIDE_ROLE_URL = "/api/v1/ratings/ride/{role}/{rideId}";
    public static final String RATINGS_ROLE_URL = "/api/v1/ratings/role/{role}";
    public static final String RATINGS_USER_URL = "/api/v1/ratings/user/{role}/{userId}";
    public static final String RATINGS_AVG_RATING_URL = "/api/v1/ratings/avg-rating/{role}/{id}";

    public static final String NOT_VALID_ROLE = "ROLE";
    public static final Long RATING_NOT_EXISTS_ID = -1L;
    public static final Long USER_NOT_EXISTS_ID = -1L;
    public static final String RIDES_ID_URL = "/api/v1/rides/";
    public static final String PASSENGERS_ID_URL = "/api/v1/passengers/";
    public static final String DRIVERS_ID_URL = "/api/v1/drivers/";

    public static final String POSTGRESQL_CONTAINER = "postgres:15.1-alpine";
    public static final String ALTER_RATING_SEQ = "ALTER SEQUENCE rating_id_seq RESTART WITH 1";
    public static final String TRUNCATE_RATING = "TRUNCATE TABLE rating";
    public static final String INSERT_NEW_RATING = "INSERT INTO rating (id, ride_id, user_id, rating, comment, role_user) " +
            "VALUES (nextval('rating_id_seq'), 1, 1, 10, 'Good', 'DRIVER')";

    public static final String MESSAGE_FIELD = "message";
    public static final String ENTITY_NOT_FOUND_BY_ID_MESSAGE = "The %s with id %s not found";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "The %s with %s not found";
    public static final String DRIVER_RATING = DRIVER_ROLE + ' ' + RATING_RESOURCE;
    public static final String RIDE_ID_EXIST = RIDE_RESOURCE + " id " + RIDE_ID;
    public static final String PASSENGER_RESOURCE = "Passenger";
    public static final String DRIVER_RESOURCE = "Driver";
    public static final String EMPTY_LIST_MESSAGE = "The Rating list is empty";

    public static final Rating NEW_RATING_DRIVER = createNewRatingDriver();

    public static final RatingRequest RATING_DRIVER_NOT_EXISTS_USER_REQUEST = new RatingRequest(RIDE_ID, VALID_NOT_EXISTS_ID, 10, "Good", RatingConstants.DRIVER_ROLE);
    public static final RatingRequest RATING_DRIVER_NOT_EXISTS_RIDE_REQUEST = new RatingRequest(VALID_NOT_EXISTS_ID, USER_ID, 10, "Good", RatingConstants.DRIVER_ROLE);

    public static Map<String, String> getNotFoundMessageMap(String resource, Object value) {
        return Map.of(MESSAGE_FIELD, String.format(ENTITY_NOT_FOUND_MESSAGE, resource, value));
    }

    public static Map<String, String> getNotFoundByIdMessageMap(String resource, Object value) {
        return Map.of(MESSAGE_FIELD, String.format(ENTITY_NOT_FOUND_BY_ID_MESSAGE, resource, value));
    }

    public static Map<String, String> getEmtpyListMessageMap() {
        return Map.of(MESSAGE_FIELD, EMPTY_LIST_MESSAGE);
    }

    private static Rating createNewRatingDriver() {
        Rating rating = new Rating();
        rating.setRideId(RIDE_ID);
        rating.setUserId(USER_ID);
        rating.setRating(10);
        rating.setComment("Good");
        rating.setUserRole(DRIVER);
        return rating;
    }

    private static Rating createRating(UserRole role) {
        Rating rating = new Rating();
        rating.setId(RATING_ID);
        rating.setRideId(RIDE_ID);
        rating.setUserId(USER_ID);
        rating.setRating(10);
        rating.setComment("Good");
        rating.setUserRole(role);
        return rating;
    }

    private static RatingResponse createRatingResponse(String role) {
        return new RatingResponse(RATING_ID, RIDE_ID, USER_ID, 10, "Good", role);
    }

    private static RatingContainerResponse createRatingContainerResponse(Page<RatingResponse> page) {
        return RatingContainerResponse.builder()
                .items(page.getContent())
                .currentPage(page.getNumber())
                .sizePage(page.getSize())
                .countPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
