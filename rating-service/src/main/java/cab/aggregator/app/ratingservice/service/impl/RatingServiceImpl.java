package cab.aggregator.app.ratingservice.service.impl;

import cab.aggregator.app.ratingservice.dto.client.DriverResponse;
import cab.aggregator.app.ratingservice.dto.client.PassengerResponse;
import cab.aggregator.app.ratingservice.dto.client.RideResponse;
import cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse;
import cab.aggregator.app.ratingservice.dto.request.RatingRequest;
import cab.aggregator.app.ratingservice.dto.request.RatingUpdateDto;
import cab.aggregator.app.ratingservice.dto.response.RatingContainerResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import cab.aggregator.app.ratingservice.entity.Rating;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import cab.aggregator.app.ratingservice.exception.AccessDeniedException;
import cab.aggregator.app.ratingservice.exception.EntityNotFoundException;
import cab.aggregator.app.ratingservice.kafka.KafkaSender;
import cab.aggregator.app.ratingservice.mapper.RatingContainerMapper;
import cab.aggregator.app.ratingservice.mapper.RatingMapper;
import cab.aggregator.app.ratingservice.repository.RatingRepository;
import cab.aggregator.app.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cab.aggregator.app.ratingservice.utility.Constants.RATING;
import static cab.aggregator.app.ratingservice.utility.Constants.RIDE;
import static cab.aggregator.app.ratingservice.utility.KeycloakConstants.EMAIL_CLAIM;
import static cab.aggregator.app.ratingservice.utility.KeycloakConstants.ROLE_ADMIN;
import static cab.aggregator.app.ratingservice.utility.MessageKeys.ACCESS_DENIED_KEY;
import static cab.aggregator.app.ratingservice.utility.MessageKeys.ENTITY_RESOURCE_NOT_FOUND_KEY;
import static cab.aggregator.app.ratingservice.utility.MessageKeys.ENTITY_WITH_ID_NOT_FOUND_KEY;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final MessageSource messageSource;
    private final RatingContainerMapper ratingContainerMapper;
    private final Validator validator;
    private final KafkaSender kafkaSender;

    @Override
    @Transactional(readOnly = true)
    public RatingResponse getRatingById(Long id) {
        return ratingMapper
                .toDto(findRatingById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public RatingResponse getRatingByRideIdAndRole(Long rideId, String role) {
        validator.checkIfExistRide(rideId, getAuthorizationHeader());
        return ratingMapper
                .toDto(findRatingByRideIdAndRole(rideId, role));
    }

    @Override
    @Transactional(readOnly = true)
    public RatingContainerResponse getAllRatings(int offset, int limit) {
        return ratingContainerMapper
                .toContainer(ratingRepository
                        .findAll(PageRequest.of(offset, limit))
                        .map(ratingMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public RatingContainerResponse getAllByUserIdAndRole(Long userId, String role, int offset, int limit) {
        validator.checkIfExistUser(userId, UserRole.valueOf(role.toUpperCase()), getAuthorizationHeader());
        return ratingContainerMapper
                .toContainer(ratingRepository
                        .findAllByUserIdAndUserRole(userId, UserRole.valueOf(role.toUpperCase())
                                , PageRequest.of(offset, limit))
                        .map(ratingMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public RatingContainerResponse getAllByRole(String role, int offset, int limit) {
        return ratingContainerMapper
                .toContainer(ratingRepository
                        .findAllByUserRole(UserRole.valueOf(role.toUpperCase()), PageRequest.of(offset, limit))
                        .map(ratingMapper::toDto));
    }

    @Override
    @Transactional
    public void deleteRating(Long id) {
        Rating rating = findRatingById(id);
        ratingRepository.delete(rating);
        AvgRatingUserResponse avgRatingUserResponse = calculateAvgRating(rating.getUserId(), rating.getUserRole());
        sendUserAvgRating(avgRatingUserResponse, rating.getUserRole());
    }

    @Override
    @Transactional
    public RatingResponse createRating(RatingRequest ratingRequest, JwtAuthenticationToken token) {
        Rating rating = ratingMapper.toEntity(ratingRequest);
        validator.checkIfExistUser(rating.getUserId(), rating.getUserRole(), getAuthorizationHeader());
        validator.checkIfExistRide(rating.getRideId(), getAuthorizationHeader());
        validator.checkIfExistRatingByRideIdAndRole(rating.getRideId(), rating.getUserRole());
        validateAccessOrThrow(rating, token);
        ratingRepository.save(rating);
        AvgRatingUserResponse avgRatingUserResponse = calculateAvgRating(rating.getUserId(), rating.getUserRole());
        sendUserAvgRating(avgRatingUserResponse, rating.getUserRole());
        return ratingMapper.toDto(rating);
    }

    @Override
    @Transactional
    public RatingResponse updateRating(Long id, RatingUpdateDto ratingUpdateDto) {
        Rating rating = findRatingById(id);
        rating.setRating(ratingUpdateDto.rating());
        rating.setComment(ratingUpdateDto.comment());
        ratingRepository.save(rating);
        AvgRatingUserResponse avgRatingUserResponse = calculateAvgRating(rating.getUserId(), rating.getUserRole());
        sendUserAvgRating(avgRatingUserResponse, rating.getUserRole());
        return ratingMapper.toDto(rating);
    }

    @Override
    @Transactional(readOnly = true)
    public AvgRatingUserResponse calculateRating(Long id, String userRole) {
        UserRole role = UserRole.valueOf(userRole.toUpperCase());
        validator.checkIfExistUser(id, role, getAuthorizationHeader());
        AvgRatingUserResponse avgRatingUserResponse = calculateAvgRating(id, role);
        sendUserAvgRating(avgRatingUserResponse, role);
        return avgRatingUserResponse;
    }

    private void validateAccessOrThrow(Rating rating, JwtAuthenticationToken token) {
        if (token.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN))) {
            return;
        }

        String userEmail = token.getToken()
                .getClaims()
                .get(EMAIL_CLAIM)
                .toString();

        RideResponse rideResponse = validator.getRideByRideId(rating.getRideId(), "Bearer " + token.getToken().getTokenValue());

        DriverResponse driverResponse = validator.getDriverResponse(rideResponse.driverId(), "Bearer " + token.getToken().getTokenValue());
        PassengerResponse passengerResponse = validator.getPassengerResponse(rideResponse.passengerId(), "Bearer " + token.getToken().getTokenValue());

        String email = switch (rating.getUserRole()) {
            case PASSENGER ->
                    validator.getPassengerResponse(
                            rating.getUserId(),
                            "Bearer " + token.getToken().getTokenValue()
                    ).email();
            case DRIVER ->
                    validator.getDriverResponse(
                            rating.getUserId(),
                            "Bearer " + token.getToken().getTokenValue()
                    ).email();
        };

        boolean isEmailValid = email.equals(userEmail);

        boolean isUserPartOfRide = userEmail.equals(driverResponse.email())
                || userEmail.equals(passengerResponse.email());

        if (!isEmailValid || !isUserPartOfRide) {
            throw new AccessDeniedException(
                    messageSource.getMessage(ACCESS_DENIED_KEY,
                            new Object[]{}, LocaleContextHolder.getLocale())
            );
        }
    }

    private String getAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        return "Bearer " + token.getToken().getTokenValue();
    }

    private AvgRatingUserResponse calculateAvgRating(Long id, UserRole userRole) {
        List<Rating> userRatings = ratingRepository.findAllByUserIdAndUserRole(id, userRole);
        double avgRating = userRatings.stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0);

        return new AvgRatingUserResponse(id.intValue(), avgRating);
    }

    private void sendUserAvgRating(AvgRatingUserResponse avgRatingUserResponse, UserRole userRole) {
        switch (userRole) {
            case DRIVER -> kafkaSender.sendAvgRatingDriver(avgRatingUserResponse);
            case PASSENGER -> kafkaSender.sendAvgRatingPassenger(avgRatingUserResponse);
        }
    }

    private Rating findRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_KEY,
                        new Object[]{RATING, id}, LocaleContextHolder.getLocale())));
    }

    private Rating findRatingByRideIdAndRole(Long rideId, String role) {
        return ratingRepository.findByRideIdAndUserRole(rideId, UserRole.valueOf(role.toUpperCase()))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(ENTITY_RESOURCE_NOT_FOUND_KEY,
                        new Object[]{role, RATING, RIDE, rideId}, LocaleContextHolder.getLocale())));
    }

}
