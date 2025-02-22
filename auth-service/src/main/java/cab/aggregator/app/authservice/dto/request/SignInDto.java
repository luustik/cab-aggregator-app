package cab.aggregator.app.authservice.dto.request;

import cab.aggregator.app.authservice.dto.validation.OnCreate;
import cab.aggregator.app.authservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static cab.aggregator.app.authservice.util.RegExp.EMAIL_PATTERN;

public record SignInDto(
        @Schema(description = "User email", example = "nvienjnb@knsb.com")
        @NotNull(message = "{email.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{email.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = EMAIL_PATTERN, message = "{email.pattern}")
        String email,

        @NotBlank(message = "{password.empty}")
        @Length(max = 255, message = "{email.length}", groups = {OnCreate.class, OnUpdate.class})
        String password
) {
}
