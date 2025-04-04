package cab.aggregator.app.ratingservice.e2e;

import cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse;
import cab.aggregator.app.ratingservice.dto.request.RatingRequest;
import cab.aggregator.app.ratingservice.dto.request.RatingUpdateDto;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static cab.aggregator.app.ratingservice.utils.RatingConstants.LOCALHOST_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_AVG_RATING_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_ID_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_RIDE_ROLE_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_URL;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CucumberSteps {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;
    private RatingRequest ratingRequest;
    private RatingUpdateDto ratingUpdateDto;

    @When("get rating with id {int}")
    public void getRatingWithId(int id) {
        response = given()
                .when()
                .get(LOCALHOST_URL + RATINGS_ID_URL, id);
    }

    @When("get rating with role {string} and rideId {long}")
    public void getPassengerWithPhone(String role, Long id) {
        response = given()
                .when()
                .get(LOCALHOST_URL + RATINGS_RIDE_ROLE_URL, role, id);
    }

    @Then("response status is {int}")
    public void responseStatusIsOk(int status) {
        response.then().statusCode(status);
    }

    @And("response body rating")
    public void responseBodyPassenger(String body) throws Exception {
        assertThat(response.as(RatingResponse.class))
                .isEqualTo(objectMapper.readValue(body, RatingResponse.class));
    }

    @When("delete rating with id {int}")
    public void deleteRatingWithId(int id) {
        response = given()
                .when()
                .delete(LOCALHOST_URL + RATINGS_ID_URL, id);
    }

    @Given("create new rating body")
    public void createNewRatingBody(String body) throws Exception {
        ratingRequest = objectMapper.readValue(body, RatingRequest.class);
    }

    @When("create new rating")
    public void createNewRating() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ratingRequest)
                .when()
                .post(LOCALHOST_URL + RATINGS_URL);
    }

    @Given("update rating body")
    public void updatePassengerBody(String body) throws Exception {
        ratingUpdateDto = objectMapper.readValue(body, RatingUpdateDto.class);
    }

    @When("update rating by id {int}")
    public void updateRatingById(int id) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ratingUpdateDto)
                .when()
                .patch(LOCALHOST_URL + RATINGS_ID_URL, id);
    }

    @When("calculate avg rating user with id {int} and role {string}")
    public void calculateAvgRatingUserWithIdAndRole(int id, String role) {
        response = given()
                .when()
                .get(LOCALHOST_URL + RATINGS_AVG_RATING_URL, role, id);
    }

    @And("response body avg rating")
    public void responseBodyAvgRating(String body) throws Exception {
        assertThat(response.as(AvgRatingUserResponse.class))
                .isEqualTo(objectMapper.readValue(body, AvgRatingUserResponse.class));
    }
}
