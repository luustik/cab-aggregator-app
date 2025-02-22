package cab.aggregator.app.passengerservice.service;

import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.dto.request.PasswordRequest;
import cab.aggregator.app.passengerservice.dto.response.PassengerContainerResponse;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface PassengerService {

    PassengerResponse getPassengerById(int id);

    PassengerContainerResponse getAllPassengersAdmin(int offset, int limit);

    PassengerContainerResponse getAllPassengers(int offset, int limit);

    PassengerResponse getPassengerByPhone(String phone);

    PassengerResponse getPassengerByEmail(String email);

    void softDeletePassenger(int id, JwtAuthenticationToken token);

    void hardDeletePassenger(int id);

    PassengerResponse createPassenger(PassengerRequest passengerRequestDto);

    PassengerResponse updatePassenger(int id, PassengerRequest driverRequestDto, JwtAuthenticationToken token);

    void updatePassword(int driverId, PasswordRequest passwordRequestDto, JwtAuthenticationToken token);
}
