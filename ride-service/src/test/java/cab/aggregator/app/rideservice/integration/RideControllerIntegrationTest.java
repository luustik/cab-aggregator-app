package cab.aggregator.app.rideservice.integration;

import cab.aggregator.app.rideservice.dto.response.RideResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static cab.aggregator.app.rideservice.integration.WireMockUtilityStub.stubForUserWhenUserExists;
import static cab.aggregator.app.rideservice.integration.WireMockUtilityStub.stubForUserWhenUserNotExists;
import static cab.aggregator.app.rideservice.utils.RideConstants.ALTER_RIDE_SEQ;
import static cab.aggregator.app.rideservice.utils.RideConstants.COST_FIELD;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_RESOURCE;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_ROLE;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_WIRE_MOCK_SERVER_PORT;
import static cab.aggregator.app.rideservice.utils.RideConstants.END_PARAM;
import static cab.aggregator.app.rideservice.utils.RideConstants.INSERT_NEW_RIDE;
import static cab.aggregator.app.rideservice.utils.RideConstants.NEW_RIDE_STATUS;
import static cab.aggregator.app.rideservice.utils.RideConstants.ORDER_DATE_TIME_FIELD;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_RESOURCE;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_ROLE;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_WIRE_MOCK_SERVER_PORT;
import static cab.aggregator.app.rideservice.utils.RideConstants.POSTGRESQL_CONTAINER;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_DATE_TIME_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_DRIVER_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_ID_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_PASSENGER_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_STATUS_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_CONTAINER_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_DRIVER_NOT_EXISTS_REQUEST;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_END_RANGE_TIME_INVALID_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_END_RANGE_TIME_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_INVALID_STATUS;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_PASSENGER_NOT_EXISTS_REQUEST;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_REQUEST;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_RESOURCE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_START_RANGE_TIME_INVALID_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_START_RANGE_TIME_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_STATUS;
import static cab.aggregator.app.rideservice.utils.RideConstants.START_PARAM;
import static cab.aggregator.app.rideservice.utils.RideConstants.TRUNCATE_RIDE;
import static cab.aggregator.app.rideservice.utils.RideConstants.UPDATED_RIDE_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.VALID_NOT_EXISTS_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.getCannotSetStatusMessageMap;
import static cab.aggregator.app.rideservice.utils.RideConstants.getNotFoundByIdMessageMap;
import static cab.aggregator.app.rideservice.utils.RideConstants.getNotFoundMessageMap;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
        statements = {
                TRUNCATE_RIDE,
                ALTER_RIDE_SEQ,
                INSERT_NEW_RIDE
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class RideControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRESQL_CONTAINER);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final WireMockServer passengerWireMockServer = new WireMockServer(PASSENGER_WIRE_MOCK_SERVER_PORT);
    private static final WireMockServer driverWireMockServer = new WireMockServer(DRIVER_WIRE_MOCK_SERVER_PORT);

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
        passengerWireMockServer.start();
        driverWireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        passengerWireMockServer.stop();
        driverWireMockServer.stop();
    }

    @Test
    void getRideById_whenRideExist_returnRideResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(RIDE_RESPONSE)));
    }

    @Test
    void getRideById_whenRideNotExist_returnStatusNotFound() throws Exception {
        given()
                .when()
                .get(RIDES_ID_URL, VALID_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(RIDE_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void getAllRides_returnRideContainerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(RIDES_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(RIDE_CONTAINER_RESPONSE)));
    }

    @Test
    void getAllRidesByDriverId_whenDriverExists_returnRideContainerResponseAndStatusOk() throws Exception {
        stubForUserWhenUserExists(DRIVER_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        given()
                .when()
                .get(RIDES_DRIVER_URL, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(RIDE_CONTAINER_RESPONSE)));
    }

    @Test
    void getAllRidesByDriverId_whenDriverNotExists_returnStatusNotFound() throws Exception {
        stubForUserWhenUserNotExists(VALID_NOT_EXISTS_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        given()
                .when()
                .get(RIDES_DRIVER_URL, VALID_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(DRIVER_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void getAllRidesByPassengerId_whenPassengerExists_returnRideContainerResponseAndStatusOk() throws Exception {
        stubForUserWhenUserExists(PASSENGER_ID, PASSENGER_ROLE, passengerWireMockServer, objectMapper);
        given()
                .when()
                .get(RIDES_PASSENGER_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(RIDE_CONTAINER_RESPONSE)));
    }

    @Test
    void getAllRidesByPassengerId_whenPassengerNotExists_returnStatusNotFound() throws Exception {
        stubForUserWhenUserNotExists(VALID_NOT_EXISTS_ID, PASSENGER_ROLE, passengerWireMockServer, objectMapper);
        given()
                .when()
                .get(RIDES_PASSENGER_URL, VALID_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void getAllRidesByStatus_whenStatusValid_returnRideContainerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(RIDES_STATUS_URL, RIDE_STATUS)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(RIDE_CONTAINER_RESPONSE)));
    }

    @Test
    void getAllRidesByStatus_whenStatusNotValid_returnStatusBadRequest() {
        given()
                .when()
                .get(RIDES_STATUS_URL, RIDE_INVALID_STATUS)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAllBetweenOrderDateTime_whenDateTimeValid_returnRideContainerResponseAndStatusOk() throws Exception {
        given()
                .params(
                        START_PARAM, RIDE_START_RANGE_TIME_STR,
                        END_PARAM, RIDE_END_RANGE_TIME_STR
                )
                .when()
                .get(RIDES_DATE_TIME_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(RIDE_CONTAINER_RESPONSE)));
    }

    @Test
    void getAllBetweenOrderDateTime_whenDateTimeNotValid_returnStatusBadRequest() {
        given()
                .params(
                        START_PARAM, RIDE_START_RANGE_TIME_INVALID_STR,
                        END_PARAM, RIDE_END_RANGE_TIME_INVALID_STR
                )
                .when()
                .get(RIDES_DATE_TIME_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deleteRideById_whenRideExist_returnStatusOk() {
        given()
                .when()
                .delete(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteRideById_whenRideNotExist_returnStatusNotfound() throws Exception {
        given()
                .when()
                .delete(RIDES_ID_URL, VALID_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(RIDE_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void createRide_whenDriverNotExists_returnStatusNotFound() throws Exception {
        stubForUserWhenUserNotExists(VALID_NOT_EXISTS_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_DRIVER_NOT_EXISTS_REQUEST)
                .when()
                .post(RIDES_URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(DRIVER_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void createRide_whenPassengerNotExists_returnStatusNotFound() throws Exception {
        stubForUserWhenUserExists(DRIVER_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        stubForUserWhenUserNotExists(VALID_NOT_EXISTS_ID, PASSENGER_ROLE, passengerWireMockServer, objectMapper);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_PASSENGER_NOT_EXISTS_REQUEST)
                .when()
                .post(RIDES_URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void createRide_returnRideResponseAndStatusCreated() throws Exception {
        jdbcTemplate.execute(TRUNCATE_RIDE);
        jdbcTemplate.execute(ALTER_RIDE_SEQ);
        stubForUserWhenUserExists(DRIVER_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        stubForUserWhenUserExists(PASSENGER_ID, PASSENGER_ROLE, passengerWireMockServer, objectMapper);
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_REQUEST)
                .when()
                .post(RIDES_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().response();

        RideResponse rideResponse = response.getBody().as(RideResponse.class);
        assertThat(rideResponse)
                .usingRecursiveComparison()
                .ignoringFields(ORDER_DATE_TIME_FIELD, COST_FIELD)
                .isEqualTo(RIDE_RESPONSE);
    }

    @Test
    void updateRide_whenRideExistAndDriverNotExist_returnStatusNotFound() throws Exception {
        stubForUserWhenUserNotExists(VALID_NOT_EXISTS_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_DRIVER_NOT_EXISTS_REQUEST)
                .when()
                .put(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(DRIVER_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void updateRide_whenRideExistAndPassengerNotExist_returnStatusNotFound() throws Exception {
        stubForUserWhenUserExists(DRIVER_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        stubForUserWhenUserNotExists(VALID_NOT_EXISTS_ID, PASSENGER_ROLE, passengerWireMockServer, objectMapper);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_PASSENGER_NOT_EXISTS_REQUEST)
                .when()
                .put(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void updateRide_returnStatusOkAndRideResponse() throws Exception {
        stubForUserWhenUserExists(DRIVER_ID, DRIVER_ROLE, driverWireMockServer, objectMapper);
        stubForUserWhenUserExists(DRIVER_ID, PASSENGER_ROLE, passengerWireMockServer, objectMapper);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_REQUEST)
                .when()
                .put(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(RIDE_RESPONSE)));
    }

    @Test
    void updateRide_whenRideNotExist_returnStatusNotFound() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_REQUEST)
                .when()
                .put(RIDES_ID_URL, VALID_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(RIDE_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void updateRideStatus_whenRideExistAndRideStatusValid_returnRideResponseAndStatusOk() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NEW_RIDE_STATUS)
                .when()
                .patch(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(UPDATED_RIDE_RESPONSE)));
    }

    @Test
    void updateRideStatus_whenRideExistAndRideStatusValidButCannotSetStatus_StatusConflict() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_STATUS)
                .when()
                .patch(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getCannotSetStatusMessageMap(RIDE_STATUS))));
    }

    @Test
    void updateRideStatus_whenRideNotExistAndRideStatusValid_returnStatusNotFound() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NEW_RIDE_STATUS)
                .when()
                .patch(RIDES_ID_URL, VALID_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(RIDE_RESOURCE, VALID_NOT_EXISTS_ID))));
    }

    @Test
    void updateRideStatus_whenRideStatusNotValid_returnStatusBadRequest() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RIDE_INVALID_STATUS)
                .when()
                .patch(RIDES_ID_URL, RIDE_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
