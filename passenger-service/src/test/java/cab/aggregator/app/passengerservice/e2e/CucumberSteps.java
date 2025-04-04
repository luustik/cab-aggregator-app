package cab.aggregator.app.passengerservice.e2e;

import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static cab.aggregator.app.passengerservice.utils.PassengerConstants.LOCALHOST_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_EMAIL_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_ID_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_PHONE_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_SAFE_ID_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_URL;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CucumberSteps {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;
    private PassengerRequest passengerRequest;

    @When("get passenger with id {int}")
    public void getPassengerWithId(int id) {
        response = given()
                .when()
                .get(LOCALHOST_URL + PASSENGERS_ID_URL, id);
    }

    @When("get passenger with phone {string}")
    public void getPassengerWithPhone(String phoneNumber) {
        response = given()
                .when()
                .get(LOCALHOST_URL + PASSENGERS_PHONE_URL, phoneNumber);
    }

    @When("get passenger with email {string}")
    public void getPassengerWithEmail(String email) {
        response = given()
                .when()
                .get(LOCALHOST_URL + PASSENGERS_EMAIL_URL, email);
    }

    @Then("response status is {int}")
    public void responseStatusIsOk(int status) {
        response.then().statusCode(status);
    }

    @And("response body passenger")
    public void responseBodyPassenger(String body) throws Exception {
        assertThat(response.as(PassengerResponse.class))
                .isEqualTo(objectMapper.readValue(body, PassengerResponse.class));
    }

    @When("soft delete passenger with id {int}")
    public void softDeletePassengerWithId(int id) {
        response = given()
                .when()
                .delete(LOCALHOST_URL + PASSENGERS_SAFE_ID_URL, id);
    }

    @When("delete passenger with id {int}")
    public void deletePassengerWithId(int id) {
        response = given()
                .when()
                .delete(LOCALHOST_URL + PASSENGERS_ID_URL, id);
    }

    @Given("create new passenger body")
    public void createNewPassengerBody(String body) throws Exception {
        passengerRequest = objectMapper.readValue(body, PassengerRequest.class);
    }

    @When("create new passenger")
    public void createNewPassenger() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(passengerRequest)
                .when()
                .post(LOCALHOST_URL + PASSENGERS_URL);
    }

    @Given("update passenger body")
    public void updatePassengerBody(String body) throws Exception {
        passengerRequest = objectMapper.readValue(body, PassengerRequest.class);
    }

    @When("update passenger by id {int}")
    public void updateDriverById(int id) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(passengerRequest)
                .when()
                .put(LOCALHOST_URL + PASSENGERS_ID_URL, id);
    }
}
