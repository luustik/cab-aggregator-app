package cab.aggregator.app.passengerservice.dto.request;

import cab.aggregator.app.passengerservice.dto.validation.OnCreate;
import cab.aggregator.app.passengerservice.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static cab.aggregator.app.passengerservice.utility.RegExp.EMAIL_PATTERN;
import static cab.aggregator.app.passengerservice.utility.RegExp.PHONE_NUMBER_PATTERN;


@Schema(description = "PassengerRequest DTO")
public record PassengerRequest(

        @Schema(description = "Passenger name", example = "Kirill")
        @NotNull(message = "{passengerName.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{passengerName.length}", groups = {OnCreate.class, OnUpdate.class})
        String name,

        @Schema(description = "Passenger email", example = "qwerty@qwe.com")
        @NotNull(message = "{passengerEmail.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{passengerEmail.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = EMAIL_PATTERN, message = "{passengerEmail.pattern}")
        String email,

        @Schema(description = "Passenger phone", example = "+375(25)1234567")
        @NotNull(message = "{passengerPhone.notnull}", groups = {OnCreate.class, OnUpdate.class})
        @Length(max = 255, message = "{passengerPhone.length}", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "{passengerPhone.pattern}")
        @JsonAlias({"phone", "phoneNumber"})
        String phone

) {
}
