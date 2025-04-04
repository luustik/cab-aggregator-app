package cab.aggregator.app.driverservice.dto.request;

import cab.aggregator.app.driverservice.dto.validation.OnCreate;
import cab.aggregator.app.driverservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import static cab.aggregator.app.driverservice.utility.RegExp.CAR_NUMBER_PATTERN;

@Schema(description = "CarRequest DTO")
public record CarRequest(
        @Schema(description = "Car model", example = "audi")
        @NotNull(message = "{model.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{model.length}", groups = {OnCreate.class, OnUpdate.class})
        String model,
        @Schema(description = "Car number", example = "1111AB-1")
        @NotNull(message = "{carNumber.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{carNumber.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = CAR_NUMBER_PATTERN, message = "{carNumber.pattern}")
        String carNumber,
        @Schema(description = "Car color", example = "pink")
        @NotNull(message = "{color.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{color.length}", groups = {OnCreate.class, OnUpdate.class})
        String color,
        @Schema(description = "Driver car Id")
        @NotNull(message = "{driverId.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Positive
        Integer driverId
) {
}
