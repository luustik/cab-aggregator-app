package cab.aggregator.app.ratingservice.service.impl;

import cab.aggregator.app.ratingservice.dto.request.RatingRequest;
import cab.aggregator.app.ratingservice.dto.request.RatingUpdateDto;
import cab.aggregator.app.ratingservice.dto.response.RatingContainerResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import cab.aggregator.app.ratingservice.entity.Rating;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import cab.aggregator.app.ratingservice.exception.EntityNotFoundException;
import cab.aggregator.app.ratingservice.exception.ResourceAlreadyExistException;
import cab.aggregator.app.ratingservice.mapper.RatingContainerMapper;
import cab.aggregator.app.ratingservice.mapper.RatingMapper;
import cab.aggregator.app.ratingservice.repository.RatingRepository;
import cab.aggregator.app.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cab.aggregator.app.ratingservice.utility.Constants.ENTITY_RESOURCE_NOT_FOUND_MESSAGE;
import static cab.aggregator.app.ratingservice.utility.Constants.ENTITY_WITH_ID_NOT_FOUND_MESSAGE;
import static cab.aggregator.app.ratingservice.utility.Constants.RATING;
import static cab.aggregator.app.ratingservice.utility.Constants.RESOURCE_ALREADY_EXISTS_MESSAGE;
import static cab.aggregator.app.ratingservice.utility.Constants.RIDE;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MessageSource messageSource;
    private final RatingMapper ratingMapper;
    private final RatingContainerMapper ratingContainerMapper;
    private final ValidatorClientService validatorClientService;

    @Override
    @Transactional(readOnly = true)
    public RatingResponse getRatingById(Long id) {
        return ratingMapper
                .toDto(findRatingById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public RatingResponse getRatingByRideIdAndRole(Long rideId, String role) {
        validatorClientService.checkIfExistRide(rideId);
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
        validatorClientService.checkIfExistUser(userId, UserRole.valueOf(role.toUpperCase()));
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
        ratingRepository.delete(findRatingById(id));
    }

    @Override
    @Transactional
    public RatingResponse createRating(RatingRequest ratingRequest) {
        Rating rating = ratingMapper.toEntity(ratingRequest);
        validatorClientService.checkIfExistUser(rating.getUserId(),rating.getUserRole());
        validatorClientService.checkIfExistRide(rating.getRideId());
        checkIfExistRatingByRideIdAndRole(rating.getRideId(), rating.getUserRole());
        ratingRepository.save(rating);
        return ratingMapper.toDto(rating);
    }

    @Override
    @Transactional
    public RatingResponse updateRating(Long id, RatingUpdateDto ratingUpdateDto) {
        Rating rating = findRatingById(id);
        rating.setRating(ratingUpdateDto.rating());
        rating.setComment(ratingUpdateDto.comment());
        ratingRepository.save(rating);
        return ratingMapper.toDto(rating);
    }

    private void checkIfExistRatingByRideIdAndRole(Long rideId, UserRole role) {
        if (ratingRepository.existsByRideIdAndUserRole(rideId, role)) {
            throw new ResourceAlreadyExistException(messageSource.getMessage(RESOURCE_ALREADY_EXISTS_MESSAGE,
                    new Object[]{RATING, RIDE, rideId, role.toString()}, LocaleContextHolder.getLocale()));
        }
    }

    private Rating findRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_MESSAGE,
                        new Object[]{RATING, id}, LocaleContextHolder.getLocale())));
    }

    private Rating findRatingByRideIdAndRole(Long rideId, String role) {
        return ratingRepository.findByRideIdAndUserRole(rideId, UserRole.valueOf(role.toUpperCase()))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(ENTITY_RESOURCE_NOT_FOUND_MESSAGE,
                        new Object[]{role, RATING, RIDE, rideId}, LocaleContextHolder.getLocale())));
    }

}
