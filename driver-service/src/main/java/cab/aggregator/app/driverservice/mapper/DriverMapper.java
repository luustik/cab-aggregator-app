package cab.aggregator.app.driverservice.mapper;

import cab.aggregator.app.driverservice.dto.request.DriverRequest;
import cab.aggregator.app.driverservice.dto.response.DriverResponse;
import cab.aggregator.app.driverservice.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    DriverResponse toDto(Driver driver);

    void updateDriverFromDto(DriverRequest driverRequestDto, @MappingTarget Driver
            driver);

    List<DriverResponse> toDtoList(List<Driver> drivers);

    Driver toEntity(DriverRequest driverRequestDto);

}
