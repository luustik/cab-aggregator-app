package cab.aggregator.app.driverservice.mapper;

import cab.aggregator.app.driverservice.dto.response.CarContainerResponse;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarContainerResponseMapper {

    default CarContainerResponse toContainer(Page<CarResponse> cars) {
        return CarContainerResponse.builder()
                .items(cars.getContent())
                .currentPage(cars.getNumber())
                .sizePage(cars.getSize())
                .countPages(cars.getTotalPages())
                .totalElements(cars.getTotalElements())
                .build();
    }
}
