package cab.aggregator.app.ratingservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVERS_ID_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVER_RESOURCE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVER_ROLE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.PASSENGERS_ID_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.PASSENGER_RESOURCE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.PASSENGER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.PASSENGER_ROLE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDES_ID_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_RESOURCE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.VALID_NOT_EXISTS_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.getNotFoundByIdMessageMap;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.getNotFoundMessageMap;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WireMockUtilityStub {

    //Ride Stubs
    public static void stubForRideService_whenRideExists(WireMockServer rideWireMockServer, ObjectMapper objectMapper) throws Exception {
        rideWireMockServer.stubFor(WireMock.get(urlPathEqualTo(RIDES_ID_URL + RIDE_ID))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(RIDE_RESPONSE))));
    }

    public static void stubForRideService_whenRideNotExists(WireMockServer rideWireMockServer, ObjectMapper objectMapper) throws Exception {
        rideWireMockServer.stubFor(WireMock.get(urlPathEqualTo(RIDES_ID_URL + VALID_NOT_EXISTS_ID))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper
                                .writeValueAsString(getNotFoundByIdMessageMap(RIDE_RESOURCE, VALID_NOT_EXISTS_ID)))));
    }

    //User Stubs
    public static void stubForUserWhenUserExists(Long userId, String role, WireMockServer server, ObjectMapper objectMapper) throws Exception {

        switch (role) {
            case DRIVER_ROLE -> stubForDriverService_whenDriverExists(userId, server, objectMapper);
            case PASSENGER_ROLE -> stubForPassengerService_whenPassengerExists(userId, server, objectMapper);
        }
    }

    public static void stubForUserWhenUserNotExists(Long userId, String role, WireMockServer server, ObjectMapper objectMapper) throws Exception {

        switch (role) {
            case DRIVER_ROLE -> stubForDriverService_whenDriverNotExists(userId, server, objectMapper);
            case PASSENGER_ROLE -> stubForPassengerService_whenPassengerNotExists(userId, server, objectMapper);
        }
    }

    //Passenger stubs
    private static void stubForPassengerService_whenPassengerExists(Long userId, WireMockServer passengerWireMockServer, ObjectMapper objectMapper) throws Exception {
        passengerWireMockServer.stubFor(WireMock.get(urlPathEqualTo(PASSENGERS_ID_URL + userId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(PASSENGER_RESPONSE))));
    }

    private static void stubForPassengerService_whenPassengerNotExists(Long userId, WireMockServer passengerWireMockServer, ObjectMapper objectMapper) throws Exception {
        passengerWireMockServer.stubFor(WireMock.get(urlPathEqualTo(PASSENGERS_ID_URL + userId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper
                                .writeValueAsString(getNotFoundByIdMessageMap(PASSENGER_RESOURCE, userId)))));
    }

    //Driver Stubs
    private static void stubForDriverService_whenDriverExists(Long userId, WireMockServer driverWireMockServer, ObjectMapper objectMapper) throws Exception {
        driverWireMockServer.stubFor(WireMock.get(urlPathEqualTo(DRIVERS_ID_URL + userId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(DRIVER_RESPONSE))));
    }

    private static void stubForDriverService_whenDriverNotExists(Long userId, WireMockServer driverWireMockServer, ObjectMapper objectMapper) throws Exception {
        driverWireMockServer.stubFor(WireMock.get(urlPathEqualTo(DRIVERS_ID_URL + userId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper
                                .writeValueAsString(getNotFoundMessageMap(DRIVER_RESOURCE, userId)))));
    }
}
