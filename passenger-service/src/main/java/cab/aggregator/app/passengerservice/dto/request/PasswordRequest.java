package cab.aggregator.app.passengerservice.dto.request;

import cab.aggregator.app.passengerservice.dto.validation.OnCreate;
import cab.aggregator.app.passengerservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "PasswordRequest DTO")
public record PasswordRequest(
        @Schema(description = "Passenger password", example = "qwerty123")
        @NotBlank(message = "{password.notblank}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{password.length}", groups = {OnCreate.class, OnUpdate.class})
        String password
) {
}
