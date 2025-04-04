package cab.aggregator.app.driverservice.integration;

import cab.aggregator.app.driverservice.kafka.KafkaConsumerConfig;
import cab.aggregator.app.driverservice.kafka.KafkaListenerService;
import cab.aggregator.app.driverservice.utils.CarConstants;
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

import static cab.aggregator.app.driverservice.utils.DriverConstants.ALTER_DRIVER_SEQ;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_ADMIN_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_GENDER_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_ID_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_SAFE_ID_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_CONTAINER_RESPONSE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_GENDER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_ID;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_INVALID_REQUEST;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_NOT_EXISTS_ID;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_REQUEST;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_RESOURCE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_RESPONSE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.INSERT_NEW_DRIVER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.TRUNCATE_DRIVER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.getNotFoundMessageMap;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({KafkaConsumerConfig.class, KafkaListenerService.class})
@Sql(
        statements = {
                TRUNCATE_DRIVER,
                ALTER_DRIVER_SEQ,
                INSERT_NEW_DRIVER
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class DriverControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = PostgresContainer.getInstance();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    void getDriverById_whenDriverExist_returnDriverResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(DRIVERS_ID_URL, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_RESPONSE)));
    }

    @Test
    void getDriverById_whenDriverNotExist_returnStatusNotFound() throws Exception {
        given()
                .when()
                .get(DRIVERS_ID_URL, DRIVER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(DRIVER_RESOURCE, DRIVER_NOT_EXISTS_ID))));
    }

    @Test
    void getAllDriversAdmin_returnDriverContainerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(DRIVERS_ADMIN_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_CONTAINER_RESPONSE)));
    }

    @Test
    void getAllDrivers_returnDriverContainerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(DRIVERS_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_CONTAINER_RESPONSE)));
    }

    @Test
    void getDriversByGender_returnDriverResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(DRIVERS_GENDER_URL, DRIVER_GENDER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_CONTAINER_RESPONSE)));
    }

    @Test
    void safeDeleteDriverById_whenDriverExist_returnStatusOk() {
        given()
                .when()
                .delete(DRIVERS_SAFE_ID_URL, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void safeDeleteDriverById_whenDriverNotExist_returnStatusNotfound() throws Exception {
        given()
                .when()
                .delete(DRIVERS_SAFE_ID_URL, DRIVER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(DRIVER_RESOURCE, DRIVER_NOT_EXISTS_ID))));
    }

    @Test
    void deleteDriverById_whenDriverExist_returnStatusOk() {
        given()
                .when()
                .delete(DRIVERS_ID_URL, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteDriverById_whenDriverNotExist_returnStatusNotfound() throws Exception {
        given()
                .when()
                .delete(DRIVERS_ID_URL, DRIVER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(DRIVER_RESOURCE, DRIVER_NOT_EXISTS_ID))));
    }

    @Test
    void createDriver_whenDriverRequestValid_returnDriverResponseAndStatusCreated() throws Exception {
        jdbcTemplate.execute(TRUNCATE_DRIVER);
        jdbcTemplate.execute(ALTER_DRIVER_SEQ);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(DRIVER_REQUEST)
                .when()
                .post(DRIVERS_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_RESPONSE)));
    }

    @Test
    void createDriver_whenDriverEmailAndPhoneNumberNotValid_returnStatusBadRequest() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(DRIVER_INVALID_REQUEST)
                .when()
                .post(DRIVERS_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateDriver_wheDriverExistAndDriverRequestValid_returnDriverResponseAndStatusOk() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(DRIVER_REQUEST)
                .when()
                .put(DRIVERS_ID_URL, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_RESPONSE)));
    }

    @Test
    void updateDriver_whenDriverNotExistAndDriverRequestValid_returnStatusNotFound() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(DRIVER_REQUEST)
                .when()
                .put(DRIVERS_ID_URL, DRIVER_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(CarConstants.getNotFoundMessageMap(DRIVER_RESOURCE, DRIVER_NOT_EXISTS_ID))));
    }

    @Test
    void updateDriver_whenDriverEmailAndPhoneNumberNotValid_returnStatusBadRequest() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(DRIVER_INVALID_REQUEST)
                .when()
                .put(DRIVERS_ID_URL, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}
