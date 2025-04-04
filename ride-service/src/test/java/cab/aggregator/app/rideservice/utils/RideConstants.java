package cab.aggregator.app.rideservice.utils;

import cab.aggregator.app.rideservice.dto.client.DriverResponse;
import cab.aggregator.app.rideservice.dto.client.PassengerResponse;
import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import cab.aggregator.app.rideservice.entity.Ride;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static cab.aggregator.app.rideservice.entity.enums.Status.CREATED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RideConstants {

    public static final int COUNT_CALLS_METHOD = 1;
    public static final int OFFSET = 0;
    public static final int LIMIT = 20;
    public static final int PASSENGER_WIRE_MOCK_SERVER_PORT = 5011;
    public static final int DRIVER_WIRE_MOCK_SERVER_PORT = 5021;

    public static final Long RIDE_ID = 1L;
    public static final Long DRIVER_ID = 1L;
    public static final Long PASSENGER_ID = 1L;
    public static final Long VALID_NOT_EXISTS_ID = 10L;
    public static final String RIDE_STATUS = "CREATED";
    public static final String NEW_RIDE_STATUS = "ACCEPTED";
    public static final String RIDE_INVALID_STATUS = "INVALID_STATUS";
    public static final BigDecimal RIDE_COST = new BigDecimal("100.00");
    public static final LocalDateTime RIDE_TIME = LocalDateTime.of(2024, 10, 15, 19, 0, 0);
    public static final LocalDateTime RIDE_START_RANGE_TIME = LocalDateTime.of(2024, 10, 1, 19, 0);
    public static final String RIDE_START_RANGE_TIME_STR = "2024-10-01 19:00";
    public static final String RIDE_START_RANGE_TIME_INVALID_STR = "qwe123";
    public static final LocalDateTime RIDE_END_RANGE_TIME = LocalDateTime.of(2024, 10, 30, 19, 0);
    public static final String RIDE_END_RANGE_TIME_STR = "2024-10-30 19:00";
    public static final String RIDE_END_RANGE_TIME_INVALID_STR = "qwe123";

    public static final Ride RIDE = createRide();

    public static final RideResponse RIDE_RESPONSE = createRideResponse();
    public static final RideResponse UPDATED_RIDE_RESPONSE = new RideResponse(RIDE_ID, DRIVER_ID, PASSENGER_ID, "departure address", "arrival address", NEW_RIDE_STATUS, RIDE_TIME, RIDE_COST);
    public static final RideRequest RIDE_REQUEST = createRideRequest();

    public static final List<Ride> RIDE_LIST = List.of(RIDE);
    public static final List<RideResponse> RIDE_RESPONSE_LIST = List.of(RIDE_RESPONSE);
    public static final Page<Ride> RIDE_PAGE = new PageImpl<>(RIDE_LIST, PageRequest.of(OFFSET, LIMIT), RIDE_LIST.size());
    public static final Page<RideResponse> RIDE_RESPONSE_PAGE = new PageImpl<>(RIDE_RESPONSE_LIST, PageRequest.of(OFFSET, LIMIT), RIDE_RESPONSE_LIST.size());
    public static final RideContainerResponse RIDE_CONTAINER_RESPONSE = createRideContainerResponse();

    public static final PassengerResponse PASSENGER_RESPONSE = new PassengerResponse(PASSENGER_ID.intValue(), "Kirill", "123@qwe.123", "+375(29)2587413", false);
    public static final DriverResponse DRIVER_RESPONSE = new DriverResponse(PASSENGER_ID.intValue(), "Kirill", "123@qwe.123", "+375(29)2587413", "MALE", false);

    public static final String IMPOSSIBLE_STATUS = "Impossible status";

    public static final String LOCALHOST_URL = "http://localhost:8888";
    public static final String RIDES_ID_URL = "/api/v1/rides/{id}";
    public static final String RIDES_URL = "/api/v1/rides";
    public static final String RIDES_DRIVER_URL = "/api/v1/rides/driver-id/{driverId}";
    public static final String RIDES_PASSENGER_URL = "/api/v1/rides/passenger-id/{passengerId}";
    public static final String RIDES_STATUS_URL = "/api/v1/rides/status/{status}";
    public static final String RIDES_DATE_TIME_URL = "/api/v1/rides/ride-between-date-time/";

    public static final String PASSENGERS_ID_URL = "/api/v1/passengers/";
    public static final String DRIVERS_ID_URL = "/api/v1/drivers/";

    public static final String POSTGRESQL_CONTAINER = "postgres:15.1-alpine";
    public static final String ALTER_RIDE_SEQ = "ALTER SEQUENCE ride_id_seq RESTART WITH 1";
    public static final String TRUNCATE_RIDE = "TRUNCATE TABLE ride";
    public static final String INSERT_NEW_RIDE = "INSERT INTO ride (id, driver_id, passenger_id, departure_address, arrival_address, status, order_date_time, cost) " +
            "VALUES (nextval('ride_id_seq'), 1, 1, 'departure address', 'arrival address', 'CREATED', '2024-10-15 19:00', '100.00')";
    public static final String MESSAGE_FIELD = "message";
    public static final String ENTITY_NOT_FOUND_BY_ID_MESSAGE = "The %s with id %s not found";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "The %s with %s not found";
    public static final String STATUS_CANNOT_SET_MESSAGE = "The status %s cannot set";
    public static final String PASSENGER_RESOURCE = "Passenger";
    public static final String DRIVER_RESOURCE = "Driver";
    public static final String RIDE_RESOURCE = "Ride";
    public static final String DRIVER_ROLE = "DRIVER";
    public static final String PASSENGER_ROLE = "PASSENGER";
    public static final String START_PARAM = "start";
    public static final String END_PARAM = "end";
    public static final String ORDER_DATE_TIME_FIELD = "orderDateTime";
    public static final String COST_FIELD = "cost";

    public static final Ride NEW_RIDE = createNewRide();

    public static final RideRequest RIDE_DRIVER_NOT_EXISTS_REQUEST = new RideRequest(VALID_NOT_EXISTS_ID, PASSENGER_ID, "departure address", "arrival address");
    public static final RideRequest RIDE_PASSENGER_NOT_EXISTS_REQUEST = new RideRequest(DRIVER_ID, VALID_NOT_EXISTS_ID, "departure address", "arrival address");

    public static Map<String, String> getNotFoundMessageMap(String resource, Object value) {
        return Map.of(MESSAGE_FIELD, String.format(ENTITY_NOT_FOUND_MESSAGE, resource, value));
    }

    public static Map<String, String> getNotFoundByIdMessageMap(String resource, Object value) {
        return Map.of(MESSAGE_FIELD, String.format(ENTITY_NOT_FOUND_BY_ID_MESSAGE, resource, value));
    }

    public static Map<String, String> getCannotSetStatusMessageMap(String status) {
        return Map.of(MESSAGE_FIELD, String.format(STATUS_CANNOT_SET_MESSAGE, status));
    }

    private static Ride createNewRide() {
        Ride ride = new Ride();
        ride.setId(RIDE_ID);
        ride.setDriverId(DRIVER_ID);
        ride.setPassengerId(PASSENGER_ID);
        ride.setDepartureAddress("departure address");
        ride.setArrivalAddress("arrival address");
        ride.setStatus(CREATED);
        ride.setOrderDateTime(RIDE_TIME);
        ride.setCost(RIDE_COST);
        return ride;
    }

    private static Ride createRide() {
        Ride ride = new Ride();
        ride.setId(RIDE_ID);
        ride.setDriverId(DRIVER_ID);
        ride.setPassengerId(PASSENGER_ID);
        ride.setDepartureAddress("departure address");
        ride.setArrivalAddress("arrival address");
        ride.setStatus(CREATED);
        ride.setOrderDateTime(RIDE_TIME);
        ride.setCost(RIDE_COST);
        return ride;
    }

    private static RideResponse createRideResponse() {
        return new RideResponse(RIDE_ID,
                DRIVER_ID,
                PASSENGER_ID,
                "departure address",
                "arrival address",
                RIDE_STATUS,
                RIDE_TIME,
                RIDE_COST);
    }

    private static RideRequest createRideRequest() {
        return new RideRequest(DRIVER_ID,
                PASSENGER_ID,
                "departure address",
                "arrival address");
    }

    private static RideContainerResponse createRideContainerResponse() {
        return RideContainerResponse.builder()
                .items(RIDE_RESPONSE_PAGE.getContent())
                .currentPage(RIDE_RESPONSE_PAGE.getNumber())
                .sizePage(RIDE_RESPONSE_PAGE.getSize())
                .countPages(RIDE_RESPONSE_PAGE.getTotalPages())
                .totalElements(RIDE_RESPONSE_PAGE.getTotalElements())
                .build();
    }
}
