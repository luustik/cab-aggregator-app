package cab.aggregator.app.driverservice.utils;

import cab.aggregator.app.driverservice.dto.request.DriverRequest;
import cab.aggregator.app.driverservice.dto.response.DriverContainerResponse;
import cab.aggregator.app.driverservice.dto.response.DriverResponse;
import cab.aggregator.app.driverservice.entity.Driver;
import cab.aggregator.app.driverservice.entity.enums.Gender;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DriverConstants {

    public static final int COUNT_CALLS_METHOD = 1;
    public static final int OFFSET = 0;
    public static final int LIMIT = 20;

    public static final String RESOURCE_ALREADY_EXISTS = "The %s with %s already exist";
    public static final String DRIVER_RESOURCE = "Driver";

    public static final Driver DRIVER = createDriver();
    public static final int DRIVER_ID = 1;
    public static final int DRIVER_NOT_EXISTS_ID = -1;
    public static final String DRIVER_EMAIL = "kkbhe@kfjbn.snb";
    public static final String DRIVER_PHONE_NUMBER = "+375(29)1234567";
    public static final String DRIVER_GENDER = "MALE";
    public static final String DRIVER_UPDATED_EMAIL = "11r@kfjbn.snb";
    public static final String DRIVER_UPDATED_PHONE_NUMBER = "+375(29)1111111";

    public static final DriverRequest DRIVER_UPDATED_REQUEST = createDriverUpdatedRequest();
    public static final DriverRequest DRIVER_INVALID_REQUEST = new DriverRequest("Pasha", "qwe123", "qwe123", DRIVER_GENDER);
    public static final DriverRequest DRIVER_REQUEST = createDriverRequest();
    public static final DriverResponse DRIVER_RESPONSE = createDriverResponse();

    public static final List<Driver> DRIVER_LIST = List.of(DRIVER);
    public static final Page<Driver> DRIVER_PAGE = new PageImpl<>(DRIVER_LIST, PageRequest.of(OFFSET, LIMIT), DRIVER_LIST.size());
    public static final List<DriverResponse> DRIVER_RESPONSE_LIST = List.of(DRIVER_RESPONSE);
    public static final Page<DriverResponse> DRIVER_RESPONSE_PAGE = new PageImpl<>(DRIVER_RESPONSE_LIST, PageRequest.of(OFFSET, LIMIT), DRIVER_RESPONSE_LIST.size());
    public static final DriverContainerResponse DRIVER_CONTAINER_RESPONSE = createDriverContainerResponse();

    public static final String LOCALHOST_URL = "http://localhost:8888";
    public static final String DRIVERS_ID_URL = "/api/v1/drivers/{id}";
    public static final String DRIVERS_URL = "/api/v1/drivers";
    public static final String DRIVERS_SAFE_ID_URL = "/api/v1/drivers/safe/{id}";
    public static final String DRIVERS_ADMIN_URL = "/api/v1/drivers/admin";
    public static final String DRIVERS_GENDER_URL = "/api/v1/drivers/driver-by-gender/{gender}";

    public static final String POSTGRESQL_CONTAINER = "postgres:15.1-alpine";
    public static final String ALTER_DRIVER_SEQ = "ALTER SEQUENCE driver_id_seq RESTART WITH 1";
    public static final String TRUNCATE_DRIVER = "TRUNCATE TABLE driver CASCADE";
    public static final String INSERT_NEW_DRIVER = "INSERT INTO driver (id, name, email, phone_number, gender, deleted, avg_grade) " +
            "VALUES (nextval('driver_id_seq'), 'Pasha', 'kkbhe@kfjbn.snb', '+375(29)1234567', 'MALE', false, 0)";

    public static final String MESSAGE_FIELD = "message";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "The %s with %s not found";

    public static Map<String, String> getNotFoundMessageMap(String resource, Object value) {
        return Map.of(MESSAGE_FIELD, String.format(ENTITY_NOT_FOUND_MESSAGE, resource, value));
    }

    private static Driver createDriver() {
        Driver driver = new Driver();
        driver.setId(DRIVER_ID);
        driver.setName("Pasha");
        driver.setEmail(DRIVER_EMAIL);
        driver.setPhoneNumber(DRIVER_PHONE_NUMBER);
        driver.setGender(Gender.MALE);
        driver.setDeleted(false);
        driver.setAvgGrade(0.0);
        return driver;
    }

    private static DriverResponse createDriverResponse() {
        return new DriverResponse(DRIVER_ID, "Pasha", DRIVER_EMAIL, DRIVER_PHONE_NUMBER, DRIVER_GENDER, false, 0.0);
    }

    private static DriverRequest createDriverRequest() {
        return new DriverRequest("Pasha", DRIVER_EMAIL, DRIVER_PHONE_NUMBER, DRIVER_GENDER);
    }

    private static DriverRequest createDriverUpdatedRequest() {
        return new DriverRequest("Pasha", DRIVER_UPDATED_EMAIL, DRIVER_UPDATED_PHONE_NUMBER, DRIVER_GENDER);
    }

    private static DriverContainerResponse createDriverContainerResponse() {
        return DriverContainerResponse.builder()
                .items(DRIVER_RESPONSE_PAGE.getContent())
                .currentPage(DRIVER_RESPONSE_PAGE.getNumber())
                .sizePage(DRIVER_RESPONSE_PAGE.getSize())
                .countPages(DRIVER_RESPONSE_PAGE.getTotalPages())
                .totalElements(DRIVER_RESPONSE_PAGE.getTotalElements())
                .build();
    }
}
