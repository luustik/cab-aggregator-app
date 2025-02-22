package cab.aggregator.app.ratingservice.controller;

import cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse;
import cab.aggregator.app.ratingservice.dto.request.RatingRequest;
import cab.aggregator.app.ratingservice.dto.request.RatingUpdateDto;
import cab.aggregator.app.ratingservice.dto.response.RatingContainerResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import cab.aggregator.app.ratingservice.dto.validation.OnCreate;
import cab.aggregator.app.ratingservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;

import static cab.aggregator.app.ratingservice.utility.RegExp.REGEXP_ROLE;

@Tag(name = "Rating Controller")
public interface RatingAPI {

    @Operation(summary = "Get rating by ID")
    public RatingResponse getRatingById(Long id);

    @Operation(summary = "Get rating by role user and ride ID")
    public RatingResponse getRatingByRideIdAndRole(@Valid @Validated
                                                   @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
                                                   String role,
                                                   Long rideId);

    @Operation(summary = "Get all ratings")
    public RatingContainerResponse getAllRatings(@Min(0) int offset,
                                                 @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all ratings by role user")
    public RatingContainerResponse getAllRatingsByRole(@Valid @Validated
                                                       @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
                                                       String role,
                                                       @Min(0) int offset,
                                                       @Min(1) @Max(100) int limit);

    @Operation(summary = "Get all ratings by role user and user ID")
    public RatingContainerResponse getAllRatingsByUserIdAndRole(@Valid @Validated
                                                                @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
                                                                String role,
                                                                Long userId,
                                                                @Min(0) int offset,
                                                                @Min(1) @Max(100) int limit);

    @Operation(summary = "Delete rating by ID")
    public void deleteRatingById(Long id);

    @Operation(summary = "Create new rating")
    public ResponseEntity<RatingResponse> createRating(@Valid @Validated(OnCreate.class) RatingRequest request, JwtAuthenticationToken token);

    @Operation(summary = "Update rating by ID")
    public RatingResponse updateRating(Long id, @Valid @Validated(OnUpdate.class) RatingUpdateDto ratingUpdateDto);

    @Operation(summary = "Calculate average rating for User")
    AvgRatingUserResponse calculateAvgRatingUser(Long id, @Valid @Validated
    @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
    String role);
}
