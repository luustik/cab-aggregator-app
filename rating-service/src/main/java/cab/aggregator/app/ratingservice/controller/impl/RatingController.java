package cab.aggregator.app.ratingservice.controller.impl;

import cab.aggregator.app.ratingservice.controller.RatingAPI;
import cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse;
import cab.aggregator.app.ratingservice.dto.request.RatingRequest;
import cab.aggregator.app.ratingservice.dto.request.RatingUpdateDto;
import cab.aggregator.app.ratingservice.dto.response.RatingContainerResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import cab.aggregator.app.ratingservice.dto.validation.OnCreate;
import cab.aggregator.app.ratingservice.dto.validation.OnUpdate;
import cab.aggregator.app.ratingservice.service.RatingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static cab.aggregator.app.ratingservice.utility.Constants.REGEXP_ROLE;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
@Validated
public class RatingController implements RatingAPI {

    private final RatingService ratingService;

    @GetMapping("/{id}")
    public RatingResponse getRatingById(@PathVariable Long id) {
        return ratingService.getRatingById(id);
    }

    @GetMapping("/ride/{role}/{rideId}")
    public RatingResponse getRatingByRideIdAndRole(@Valid @Validated
                                                   @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
                                                   @PathVariable String role,
                                                   @PathVariable Long rideId) {
        return ratingService.getRatingByRideIdAndRole(rideId, role);
    }

    @GetMapping
    public RatingContainerResponse getAllRatings(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                 @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return ratingService.getAllRatings(offset, limit);
    }

    @GetMapping("/role/{role}")
    public RatingContainerResponse getAllRatingsByRole(@Valid @Validated
                                                       @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
                                                       @PathVariable String role,
                                                       @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                       @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return ratingService.getAllByRole(role, offset, limit);
    }

    @GetMapping("/user/{role}/{userId}")
    public RatingContainerResponse getAllRatingsByUserIdAndRole(@Valid @Validated
                                                                @Pattern(regexp = REGEXP_ROLE, message = "{role.user.pattern}")
                                                                @PathVariable String role,
                                                                @PathVariable Long userId,
                                                                @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                                                @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return ratingService.getAllByUserIdAndRole(userId, role, offset, limit);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRatingById(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public ResponseEntity<RatingResponse> createRating(@Valid @Validated(OnCreate.class) @RequestBody RatingRequest request, JwtAuthenticationToken token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ratingService
                        .createRating(request, token));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RatingResponse updateRating(@PathVariable Long id,
                                       @Valid @Validated(OnUpdate.class)
                                       @RequestBody RatingUpdateDto ratingUpdateDto) {
        return ratingService.updateRating(id, ratingUpdateDto);
    }

    @GetMapping("/avg-rating/{role}/{id}")
    public AvgRatingUserResponse calculateAvgRatingUser(@PathVariable Long id, @PathVariable String role) {
        return ratingService.calculateRating(id, role);
    }
}
