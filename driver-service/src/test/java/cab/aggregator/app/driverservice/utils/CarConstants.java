package cab.aggregator.app.driverservice.utils;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.dto.response.CarContainerResponse;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import cab.aggregator.app.driverservice.entity.Car;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_ID;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_NOT_EXISTS_ID;
import static cab.aggregator.app.driverservice.utils.DriverConstants.LIMIT;
import static cab.aggregator.app.driverservice.utils.DriverConstants.OFFSET;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CarConstants {

    public static final int COUNT_CALLS_METHOD = 1;
    public static final int CAR_ID = 1;
    public static final int CAR_NOT_EXISTS_ID = -1;

    public static final String CAR_NUMBER = "7930AB-7";
    public static final String CAR_NOT_EXISTS_NUMBER = "1111AB-7";
    public static final String INVALID_CAR_NUMBER = "qwe123";
    public static final String CAR_RESOURCE = "Car";

    public static final String MESSAGE_FIELD = "message";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "The %s with %s not found";

    public static final String CARS_ID_URL = "/api/v1/cars/{id}";
    public static final String CARS_URL = "/api/v1/cars";
    public static final String CARS_BY_NUMBER_URL = "/api/v1/cars/car-by-number/{carNumber}";
    public static final String CARS_DRIVER_URL = "/api/v1/cars/cars-driver/{driverId}";

    public static final String POSTGRESQL_CONTAINER = "postgres:15.1-alpine";
    public static final String ALTER_CAR_SEQ = "ALTER SEQUENCE car_id_seq RESTART WITH 1";
    public static final String TRUNCATE_CAR = "TRUNCATE TABLE car";
    public static final String INSERT_NEW_CAR = "INSERT INTO car (id, color, model, car_number, driver_id) VALUES (nextval('car_id_seq'), 'red', 'audi', '7930AB-7', 1)";

    public static final Car CAR = createCar();

    public static final CarRequest CAR_REQUEST = createCarRequest(CAR_NUMBER, DRIVER_ID);
    public static final CarRequest CAR_UPDATED_REQUEST = createCarRequest("1111AA-1", DRIVER_ID);
    public static final CarRequest CAR_INVALID_REQUEST = createCarRequest(INVALID_CAR_NUMBER, DRIVER_NOT_EXISTS_ID);

    public static final CarResponse CAR_RESPONSE = createCarResponse();

    public static final List<Car> CAR_LIST = List.of(CAR);
    public static final Page<Car> CAR_PAGE = new PageImpl<>(CAR_LIST, PageRequest.of(OFFSET, LIMIT), CAR_LIST.size());
    public static final List<CarResponse> CAR_RESPONSE_LIST = List.of(CAR_RESPONSE);
    public static final Page<CarResponse> CAR_RESPONSE_PAGE = new PageImpl<>(CAR_RESPONSE_LIST, PageRequest.of(OFFSET, LIMIT), CAR_RESPONSE_LIST.size());
    public static final CarContainerResponse CAR_CONTAINER_RESPONSE = createCarContainerResponse();

    public static Map<String, String> getNotFoundMessageMap(String resource, Object value) {
        return Map.of(MESSAGE_FIELD, String.format(ENTITY_NOT_FOUND_MESSAGE, resource, value));
    }

    private static Car createCar() {
        Car car = new Car();
        car.setId(CAR_ID);
        car.setColor("red");
        car.setModel("audi");
        car.setCarNumber(CAR_NUMBER);
        car.setDriver(DRIVER);
        return car;
    }

    private static CarContainerResponse createCarContainerResponse() {
        return CarContainerResponse.builder()
                .items(CAR_RESPONSE_PAGE.getContent())
                .currentPage(CAR_RESPONSE_PAGE.getNumber())
                .sizePage(CAR_RESPONSE_PAGE.getSize())
                .countPages(CAR_RESPONSE_PAGE.getTotalPages())
                .totalElements(CAR_RESPONSE_PAGE.getTotalElements())
                .build();
    }

    private static CarResponse createCarResponse() {
        return new CarResponse(CAR_ID, "red", "audi", CAR_NUMBER, DRIVER_ID);
    }

    private static CarRequest createCarRequest(String carNumber, int driverId) {
        return new CarRequest("audi", carNumber, "red", driverId);
    }
}
