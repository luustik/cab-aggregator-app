package cab.aggregator.app.driverservice.dto.request;

import cab.aggregator.app.driverservice.dto.validation.OnCreate;
import cab.aggregator.app.driverservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static cab.aggregator.app.driverservice.utility.RegExp.EMAIL_PATTERN;
import static cab.aggregator.app.driverservice.utility.RegExp.GENDER_PATTERN;
import static cab.aggregator.app.driverservice.utility.RegExp.PHONE_NUMBER_PATTERN;

@Schema(description = "DriverRequest DTO")
public record DriverRequest(
        @Schema(description = "Driver name", example = "Pasha")
        @NotNull(message = "{name.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{name.length}", groups = {OnCreate.class, OnUpdate.class})
        String name,
        @Schema(description = "Driver email", example = "nvienjnb@knsb.com")
        @NotNull(message = "{email.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{email.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = EMAIL_PATTERN, message = "{email.pattern}")
        String email,
        @Schema(description = "Driver phone number", example = "+375(29)1234567")
        @NotNull(message = "{phoneNumber.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{phoneNumber.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "{phoneNumber.pattern}")
        String phoneNumber,
        @Schema(description = "Driver gender", example = "FEMALE/MALE")
        @NotNull(message = "{gender.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{gender.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = GENDER_PATTERN, message = "{gender.pattern}")
        String gender
) {
}
