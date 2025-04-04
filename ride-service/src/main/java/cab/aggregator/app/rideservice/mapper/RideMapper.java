package cab.aggregator.app.rideservice.mapper;

import cab.aggregator.app.rideservice.dto.request.RideRequest;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import cab.aggregator.app.rideservice.entity.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RideMapper {

    RideResponse toDto(Ride ride);

    void updateRideFromDto(RideRequest carRequestDto, @MappingTarget Ride ride);

    Ride toEntity(RideRequest rideRequestDto);
}
