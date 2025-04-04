package cab.aggregator.app.passengerservice.integration;

import cab.aggregator.app.passengerservice.kafka.KafkaConsumerConfig;
import cab.aggregator.app.passengerservice.kafka.KafkaListenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static cab.aggregator.app.passengerservice.utils.PassengerConstants.ALTER_PASSENGER_SEQ;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.INSERT_NEW_PASSENGER;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_ADMIN_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_EMAIL_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_ID_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_PHONE_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_SAFE_ID_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_CONTAINER_RESPONSE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_EMAIL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_ID;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_INVALID_EMAIL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_INVALID_PHONE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_INVALID_REQUEST;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_NOT_EXISTS_ID;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_PHONE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_REQUEST;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_RESOURCE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_RESPONSE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_UPDATED_EMAIL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_UPDATED_PHONE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.POSTGRESQL_CONTAINER;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.TRUNCATE_PASSENGER;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.getNotFoundByIdMessageMap;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.getNotFoundMessageMap;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({KafkaConsumerConfig.class, KafkaListenerService.class})
@Sql(
        statements = {
                TRUNCATE_PASSENGER,
                ALTER_PASSENGER_SEQ,
                INSERT_NEW_PASSENGER
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class PassengerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRESQL_CONTAINER);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    void getPassengerById_whenPassengerExist_returnPassengerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(PASSENGERS_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE)));
    }

    @Test
    void getPassengerById_whenPassengerNotExist_returnStatusNotFound() throws Exception {
        given()
                .when()
                .get(PASSENGERS_ID_URL, PASSENGER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, PASSENGER_NOT_EXISTS_ID))));
    }

    @Test
    void getPassengerByPhone_whenPassengerExist_returnPassengerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(PASSENGERS_PHONE_URL, PASSENGER_PHONE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE)));
    }

    @Test
    void getPassengerByPhone_whenPassengerNotExist_returnStatusNotFound() throws Exception {
        given()
                .when()
                .get(PASSENGERS_PHONE_URL, PASSENGER_UPDATED_PHONE)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(PASSENGER_RESOURCE, PASSENGER_UPDATED_PHONE))));
    }

    @Test
    void getPassengerByPhone_whenPhoneNotValid_returnStatusBadRequest() {
        given()
                .when()
                .get(PASSENGERS_PHONE_URL, PASSENGER_INVALID_PHONE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getPassengerByEmail_whenPassengerExist_returnPassengerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(PASSENGERS_EMAIL_URL, PASSENGER_EMAIL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE)));
    }

    @Test
    void getPassengerByEmail_whenPassengerNotExist_returnStatusNotFound() throws Exception {
        given()
                .when()
                .get(PASSENGERS_EMAIL_URL, PASSENGER_UPDATED_EMAIL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(PASSENGER_RESOURCE, PASSENGER_UPDATED_EMAIL))));
    }

    @Test
    void getPassengerByEmail_whenEmailNotValid_returnStatusBadRequest() {
        given()
                .when()
                .get(PASSENGERS_EMAIL_URL, PASSENGER_INVALID_EMAIL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAllPassengersAdmin_returnPassengerContainerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(PASSENGERS_ADMIN_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(PASSENGER_CONTAINER_RESPONSE)));
    }

    @Test
    void getAllPassengers_returnPassengerContainerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(PASSENGERS_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(PASSENGER_CONTAINER_RESPONSE)));
    }

    @Test
    void softDeletePassengerById_whenPassengerExist_returnStatusOk() {
        given()
                .when()
                .delete(PASSENGERS_SAFE_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void softDeletePassengerById_whenPassengerNotExist_returnStatusNotfound() throws Exception {
        given()
                .when()
                .delete(PASSENGERS_SAFE_ID_URL, PASSENGER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, PASSENGER_NOT_EXISTS_ID))));
    }

    @Test
    void hardDeletePassengerById_whenPassengerExist_returnStatusOk() {
        given()
                .when()
                .delete(PASSENGERS_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void hardDeletePassengerById_whenPassengerNotExist_returnStatusNotfound() throws Exception {
        given()
                .when()
                .delete(PASSENGERS_ID_URL, PASSENGER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, PASSENGER_NOT_EXISTS_ID))));
    }

    @Test
    void createPassenger_whenPassengerRequestValid_returnPassengerResponseAndStatusCreated() throws Exception {
        jdbcTemplate.execute(TRUNCATE_PASSENGER);
        jdbcTemplate.execute(ALTER_PASSENGER_SEQ);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(PASSENGER_REQUEST)
                .when()
                .post(PASSENGERS_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE)));
    }

    @Test
    void createPassenger_whenPassengerEmailAndPhoneNotValid_returnStatusBadRequest() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(PASSENGER_INVALID_REQUEST)
                .when()
                .post(PASSENGERS_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updatePassenger_whenPassengerExistAndPassengerRequestValid_returnPassengerResponseAndStatusOk() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(PASSENGER_REQUEST)
                .when()
                .put(PASSENGERS_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE)));
    }

    @Test
    void updatePassenger_whenPassengerNotExistAndPassengerRequestValid_returnStatusNotFound() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(PASSENGER_REQUEST)
                .when()
                .put(PASSENGERS_ID_URL, PASSENGER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, PASSENGER_NOT_EXISTS_ID))));
    }

    @Test
    void updatePassenger_whenPassengerEmailAndPhoneNotValid_returnStatusBadRequest() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(PASSENGER_INVALID_REQUEST)
                .when()
                .put(PASSENGERS_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
