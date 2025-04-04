package cab.aggregator.app.rideservice.service;

import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface RideService {

    RideResponse getRideById(Long rideId);

    RideContainerResponse getAllRides(int offset, int limit);

    RideContainerResponse getAllRidesByDriverId(Long driverId, int offset, int limit);

    RideContainerResponse getAllRidesByStatus(String status, int offset, int limit);

    RideContainerResponse getAllRidesByPassengerId(Long passengerId, int offset, int limit);

    RideContainerResponse getAllBetweenOrderDateTime(String start, String end, int offset, int limit);

    void deleteRide(Long id);

    RideResponse createRide(RideRequest rideRequest, JwtAuthenticationToken token);

    RideResponse updateRideStatus(Long id, String status, JwtAuthenticationToken token);

    RideResponse updateRide(Long id, RideRequest rideRequest);
}
