package cab.aggregator.app.passengerservice.mapper;

import cab.aggregator.app.passengerservice.dto.response.PassengerContainerResponse;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerContainerMapper {

    default PassengerContainerResponse toContainer(Page<PassengerResponse> passengers) {
        return PassengerContainerResponse.builder()
                .items(passengers.getContent())
                .currentPage(passengers.getNumber())
                .sizePage(passengers.getSize())
                .countPages(passengers.getTotalPages())
                .totalElements(passengers.getTotalElements())
                .build();
    }

}
