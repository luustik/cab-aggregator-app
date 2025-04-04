package cab.aggregator.app.ratingservice.dto.request;

import cab.aggregator.app.ratingservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Rating Update Request DTO")
public record RatingUpdateDto(

        @Schema(description = "Rating")
        @Min(1)
        @Max(10)
        @NotNull(message = "{rating.notnull}", groups = OnUpdate.class)
        Integer rating,

        @Schema(description = "Comment")
        @Length(max = 255, message = "{comment.length}", groups = OnUpdate.class)
        String comment
) {
}
