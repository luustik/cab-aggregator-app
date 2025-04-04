package cab.aggregator.app.passengerservice.mapper;

import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import cab.aggregator.app.passengerservice.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    PassengerResponse toDto(Passenger passenger);

    void updatePassengerFromDto(PassengerRequest passengerRequestDto, @MappingTarget Passenger
            passenger);

    List<PassengerResponse> toDtoList(List<Passenger> passengers);

    Passenger toEntity(PassengerRequest passengerRequestDto);
}
