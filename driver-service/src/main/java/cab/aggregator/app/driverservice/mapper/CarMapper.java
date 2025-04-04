package cab.aggregator.app.driverservice.mapper;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.dto.response.CarResponse;
import cab.aggregator.app.driverservice.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    @Mapping(source = "driver.id", target = "driverId")
    CarResponse toDto(Car car);

    void updateCarFromDto(CarRequest carRequestDto, @MappingTarget Car
            car);

    List<CarResponse> toDtoList(List<Car> cars);

    Car toEntity(CarRequest carRequestDto);

}
