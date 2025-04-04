package cab.aggregator.app.driverservice.mapper;

import cab.aggregator.app.driverservice.dto.response.DriverContainerResponse;
import cab.aggregator.app.driverservice.dto.response.DriverResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverContainerResponseMapper {

    default DriverContainerResponse toContainer(Page<DriverResponse> drivers) {
        return DriverContainerResponse.builder()
                .items(drivers.getContent())
                .currentPage(drivers.getNumber())
                .sizePage(drivers.getSize())
                .countPages(drivers.getTotalPages())
                .totalElements(drivers.getTotalElements())
                .build();
    }
}
