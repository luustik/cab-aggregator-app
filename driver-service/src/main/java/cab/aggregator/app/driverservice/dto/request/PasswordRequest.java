package cab.aggregator.app.driverservice.dto.request;

import cab.aggregator.app.driverservice.dto.validation.OnCreate;
import cab.aggregator.app.driverservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PasswordRequest(
        @Schema(description = "Driver password", example = "qwerty123")
        @NotBlank(message = "{password.notblank}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{password.length}", groups = {OnCreate.class, OnUpdate.class})
        String password
) {
}
