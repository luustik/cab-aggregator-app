package cab.aggregator.app.rideservice.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVERS_ID_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_RESOURCE;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_ROLE;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGERS_ID_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_RESOURCE;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_ROLE;
import static cab.aggregator.app.rideservice.utils.RideConstants.getNotFoundByIdMessageMap;
import static cab.aggregator.app.rideservice.utils.RideConstants.getNotFoundMessageMap;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WireMockUtilityStub {

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
