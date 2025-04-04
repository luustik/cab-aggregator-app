package cab.aggregator.app.driverservice.integration;

import cab.aggregator.app.driverservice.kafka.KafkaConsumerConfig;
import cab.aggregator.app.driverservice.kafka.KafkaListenerService;
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

import static cab.aggregator.app.driverservice.utils.CarConstants.ALTER_CAR_SEQ;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_BY_NUMBER_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_DRIVER_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_ID_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_CONTAINER_RESPONSE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_ID;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_INVALID_REQUEST;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_NOT_EXISTS_ID;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_NOT_EXISTS_NUMBER;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_NUMBER;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_REQUEST;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_RESOURCE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_RESPONSE;
import static cab.aggregator.app.driverservice.utils.CarConstants.INSERT_NEW_CAR;
import static cab.aggregator.app.driverservice.utils.CarConstants.TRUNCATE_CAR;
import static cab.aggregator.app.driverservice.utils.CarConstants.getNotFoundMessageMap;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({KafkaConsumerConfig.class, KafkaListenerService.class})
@Sql(
        statements = {
                TRUNCATE_CAR,
                ALTER_CAR_SEQ,
                INSERT_NEW_CAR
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class CarControllerIntegrationTest {

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
    void getCarById_whenCarExist_returnCarResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(CARS_ID_URL, CAR_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(CAR_RESPONSE)));
    }

    @Test
    void getCarById_whenCarNotExist_returnStatusNotFound() throws Exception {
        given()
                .when()
                .get(CARS_ID_URL, CAR_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(CAR_RESOURCE, CAR_NOT_EXISTS_ID))));
    }

    @Test
    void getAllCars_returnCarContainerResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(CARS_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(CAR_CONTAINER_RESPONSE)));
    }

    @Test
    void getCarByCarNumber_whenCarExist_returnCarResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(CARS_BY_NUMBER_URL, CAR_NUMBER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(CAR_RESPONSE)));
    }

    @Test
    void getCarByCarNumber_whenCarNotExist_returnStatusNotFound() throws Exception {
        given()
                .when()
                .get(CARS_BY_NUMBER_URL, CAR_NOT_EXISTS_NUMBER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(CAR_RESOURCE, CAR_NOT_EXISTS_NUMBER))));
    }

    @Test
    void getAllCarsByDriverId_returnCarResponseAndStatusOk() throws Exception {
        given()
                .when()
                .get(CARS_DRIVER_URL, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(CAR_CONTAINER_RESPONSE)));
    }

    @Test
    void deleteCar_whenCarExist_returnStatusOk() {
        given()
                .when()
                .delete(CARS_ID_URL, CAR_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteCar_whenCarNotExist_returnStatusNotfound() throws Exception {
        given()
                .when()
                .get(CARS_ID_URL, CAR_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(CAR_RESOURCE, CAR_NOT_EXISTS_ID))));
    }

    @Test
    void createCar_whenCarRequestValid_returnCarResponseAndStatusCreated() throws Exception {
        jdbcTemplate.execute(TRUNCATE_CAR);
        jdbcTemplate.execute(ALTER_CAR_SEQ);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(CAR_REQUEST)
                .when()
                .post(CARS_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(CAR_RESPONSE)));
    }

    @Test
    void createCar_whenCarNumberAndDriverIdNotValid_returnStatusBadRequest() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(CAR_INVALID_REQUEST)
                .when()
                .post(CARS_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateCar_whenCarExistAndCarRequestValid_returnCarResponseAndStatusOk() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(CAR_REQUEST)
                .when()
                .put(CARS_ID_URL, CAR_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(CAR_RESPONSE)));
    }

    @Test
    void updateCar_whenCarNotExistAndCarRequestValid_returnStatusNotFound() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(CAR_REQUEST)
                .when()
                .put(CARS_ID_URL, CAR_NOT_EXISTS_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(equalTo(objectMapper.writeValueAsString(getNotFoundMessageMap(CAR_RESOURCE, CAR_NOT_EXISTS_ID))));
    }

    @Test
    void updateCar_whenCarNumberAndDriverIdNotValid_returnStatusBadRequest() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(CAR_INVALID_REQUEST)
                .when()
                .put(CARS_ID_URL, CAR_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
