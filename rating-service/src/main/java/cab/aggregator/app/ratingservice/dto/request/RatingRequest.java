package cab.aggregator.app.ratingservice.dto.request;

import cab.aggregator.app.ratingservice.dto.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static cab.aggregator.app.ratingservice.utility.RegExp.REGEXP_ROLE;


@Schema(description = "Rating Request DTO")
public record RatingRequest(

        @Schema(description = "Ride id")
        @Positive
        @NotNull(message = "{ride.id.notnull}", groups = OnCreate.class)
        Long rideId,

        @Schema(description = "User id")
        @Positive
        @NotNull(message = "{user.id.notnull}", groups = OnCreate.class)
        Long userId,

        @Schema(description = "Rating")
        @Min(1)
        @Max(10)
        @NotNull(message = "{rating.notnull}", groups = OnCreate.class)
        Integer rating,

        @Schema(description = "Comment")
        @Length(max = 255, message = "{comment.length}")
        String comment,

        @Schema(description = "Role user")
        @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
        String userRole
) {
}
