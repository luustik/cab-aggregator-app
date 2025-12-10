package cab.aggregator.app.authservice.dto.request;

import cab.aggregator.app.authservice.dto.validation.OnCreate;
import cab.aggregator.app.authservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static cab.aggregator.app.authservice.util.RegExp.EMAIL_PATTERN;
import static cab.aggregator.app.authservice.util.RegExp.GENDER_PATTERN;
import static cab.aggregator.app.authservice.util.RegExp.PHONE_NUMBER_PATTERN;
import static cab.aggregator.app.authservice.util.RegExp.ROLE_PATTERN;

@Schema(description = "SignUp DTO")
public record SignUpDto(

        @Schema(description = "User name", example = "Pasha")
        @NotNull(message = "{name.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{name.length}", groups = {OnCreate.class, OnUpdate.class})
        String name,

        @Schema(description = "User email", example = "nvienjnb@knsb.com")
        @NotNull(message = "{email.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{email.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = EMAIL_PATTERN, message = "{email.pattern}")
        String email,

        @Schema(description = "User phone number", example = "+375(29)1234567")
        @NotNull(message = "{phoneNumber.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{phoneNumber.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "{phoneNumber.pattern}")
        String phoneNumber,

        @Schema(description = "User gender", example = "FEMALE/MALE")
        @NotNull(message = "{gender.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{gender.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = GENDER_PATTERN, message = "{gender.pattern}")
        String gender,

        @Schema(description = "User role", example = "DRIVER/PASSENGER")
        @NotNull(message = "{role.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = ROLE_PATTERN, message = "{role.pattern}")
        String role,

        @Schema(description = "Password", example = "qwerty123")
        @NotBlank(message = "{password.empty}")
        @Length(max = 255, message = "{password.length}", groups = {OnCreate.class, OnUpdate.class})
        String password
) {
}
