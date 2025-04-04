package cab.aggregator.app.rideservice.e2e;

import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static cab.aggregator.app.rideservice.utils.RideConstants.COST_FIELD;
import static cab.aggregator.app.rideservice.utils.RideConstants.LOCALHOST_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.ORDER_DATE_TIME_FIELD;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_ID_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_URL;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CucumberSteps {

    private final ObjectMapper objectMapper = objectMapper();
    private Response response;
    private RideRequest rideRequest;
    private String status;

    @When("get ride with id {int}")
    public void getRideWithId(int id) {
        response = given()
                .when()
                .get(LOCALHOST_URL + RIDES_ID_URL, id);
    }

    @Then("response status is {int}")
    public void responseStatusIsOk(int status) {
        response.then().statusCode(status);
    }

    @And("response body ride for get method")
    public void responseBodyRideForGet(String body) throws Exception {
        assertThat(response.as(RideResponse.class))
                .isEqualTo(objectMapper.readValue(body, RideResponse.class));
    }

    @And("response body ride")
    public void responseBodyRide(String body) throws Exception {
        assertThat(response.as(RideResponse.class))
                .usingRecursiveComparison()
                .ignoringFields(ORDER_DATE_TIME_FIELD, COST_FIELD)
                .isEqualTo(objectMapper.readValue(body, RideResponse.class));
    }

    @When("delete ride with id {int}")
    public void deleteRideWithId(int id) {
        response = given()
                .when()
                .delete(LOCALHOST_URL + RIDES_ID_URL, id);
    }

    @Given("create new ride body")
    public void createNewRatingBody(String body) throws Exception {
        rideRequest = objectMapper.readValue(body, RideRequest.class);
    }

    @When("create new ride")
    public void createNewRide() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(rideRequest)
                .when()
                .post(LOCALHOST_URL + RIDES_URL);
    }

    @Given("update ride body")
    public void updateRideBody(String body) throws Exception {
        rideRequest = objectMapper.readValue(body, RideRequest.class);
    }

    @When("update ride by id {long}")
    public void updateRideById(Long id) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(rideRequest)
                .when()
                .put(LOCALHOST_URL + RIDES_ID_URL, id);
    }

    @Given("updated ride status")
    public void updatedRideStatus(String body) throws Exception {
        status = objectMapper.readValue(body, String.class);
    }

    @When("update ride status by id {int}")
    public void updateRideStatusById(int id) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(status)
                .when()
                .patch(LOCALHOST_URL + RIDES_ID_URL, id);
    }

    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
