package cab.aggregator.app.driverservice.e2e;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.dto.request.DriverRequest;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import cab.aggregator.app.driverservice.dto.response.DriverResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_BY_NUMBER_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_ID_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_ID_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_SAFE_ID_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVERS_URL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.LOCALHOST_URL;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CucumberSteps {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;
    private DriverRequest driverRequest;
    private CarRequest carRequest;

    @When("get driver with id {int}")
    public void getDriverWithId(int id) {
        response = given()
                .when()
                .get(LOCALHOST_URL + DRIVERS_ID_URL, id);
    }

    @Then("response status is {int}")
    public void responseStatusIsOk(int status) {
        response.then().statusCode(status);
    }

    @And("response body contain driver")
    public void responseBodyContainDriver(String body) throws Exception {
        assertThat(response.as(DriverResponse.class))
                .isEqualTo(objectMapper.readValue(body, DriverResponse.class));
    }

    @When("safe delete driver with id {int}")
    public void safeDeleteDriverWithId(int id) {
        response = given()
                .when()
                .delete(LOCALHOST_URL + DRIVERS_SAFE_ID_URL, id);
    }

    @When("delete driver with id {int}")
    public void deleteDriverWithId(int id) {
        response = given()
                .when()
                .delete(LOCALHOST_URL + DRIVERS_ID_URL, id);
    }

    @Given("create new driver body")
    public void createNewDriverBody(String body) throws Exception {
        driverRequest = objectMapper.readValue(body, DriverRequest.class);
    }

    @When("create new driver")
    public void createNewDriver() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(driverRequest)
                .when()
                .post(LOCALHOST_URL + DRIVERS_URL);
    }

    @Given("update driver body")
    public void updateDriverBody(String body) throws Exception {
        driverRequest = objectMapper.readValue(body, DriverRequest.class);
    }

    @When("update driver by id {int}")
    public void updateDriverById(int id) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(driverRequest)
                .when()
                .put(LOCALHOST_URL + DRIVERS_ID_URL, id);
    }

    @When("get car with id {int}")
    public void getCarWithId(int id) {
        response = given()
                .when()
                .get(LOCALHOST_URL + CARS_ID_URL, id);
    }

    @When("get car with carNumber {string}")
    public void getCarWithId(String carNumber) {
        response = given()
                .when()
                .get(LOCALHOST_URL + CARS_BY_NUMBER_URL, carNumber);
    }

    @And("response body car")
    public void responseBodyCar(String body) throws Exception {
        assertThat(response.as(CarResponse.class))
                .isEqualTo(objectMapper.readValue(body, CarResponse.class));
    }

    @Given("create new car body")
    public void createNewCarBody(String body) throws Exception {
        carRequest = objectMapper.readValue(body, CarRequest.class);
    }

    @When("create new car")
    public void createNewCar() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(carRequest)
                .when()
                .post(LOCALHOST_URL + CARS_URL);
    }

    @Given("update car body")
    public void updateCarBody(String body) throws Exception {
        carRequest = objectMapper.readValue(body, CarRequest.class);
    }

    @When("update car by id {int}")
    public void updateCarById(int id) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(carRequest)
                .when()
                .put(LOCALHOST_URL + CARS_ID_URL, id);
    }

    @When("delete car with id {int}")
    public void deleteCarWithId(int id) {
        response = given()
                .when()
                .delete(LOCALHOST_URL + CARS_ID_URL, id);
    }
}
