package cab.aggregator.app.rideservice.mapper;

import cab.aggregator.app.rideservice.dto.response.RideContainerResponse;
import cab.aggregator.app.rideservice.dto.response.RideResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RideContainerMapper {

    default RideContainerResponse toContainer(Page<RideResponse> rides) {
        return RideContainerResponse.builder()
                .items(rides.getContent())
                .currentPage(rides.getNumber())
                .sizePage(rides.getSize())
                .countPages(rides.getTotalPages())
                .totalElements(rides.getTotalElements())
                .build();
    }
}
