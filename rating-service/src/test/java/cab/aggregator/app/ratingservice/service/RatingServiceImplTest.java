package cab.aggregator.app.ratingservice.service;

import cab.aggregator.app.ratingservice.dto.kafka.AvgRatingUserResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingContainerResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import cab.aggregator.app.ratingservice.exception.EmptyListException;
import cab.aggregator.app.ratingservice.exception.EntityNotFoundException;
import cab.aggregator.app.ratingservice.kafka.KafkaSender;
import cab.aggregator.app.ratingservice.mapper.RatingContainerMapper;
import cab.aggregator.app.ratingservice.mapper.RatingMapper;
import cab.aggregator.app.ratingservice.repository.RatingRepository;
import cab.aggregator.app.ratingservice.service.impl.RatingServiceImpl;
import cab.aggregator.app.ratingservice.service.impl.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;


import static cab.aggregator.app.ratingservice.utils.RatingConstants.AVG_RATING_USER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVER_ROLE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.LIMIT;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.LIST_EMPTY;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.OFFSET;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_CONTAINER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_CONTAINER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_LIST;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_PAGE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_REQUEST;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_RESPONSE_PAGE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_UPDATED_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_PAGE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_PASSENGER;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_PASSENGER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_RESOURCE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_RESPONSE_PAGE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_UPDATE_DTO;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private RatingContainerMapper ratingContainerResponseMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Validator validator;

    @Mock
    private KafkaSender kafkaSender;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void getRatingById_whenRatingExists_returnRatingResponse() {
        when(ratingRepository.findById(RATING_ID))
                .thenReturn(Optional.of(RATING_DRIVER));
        when(ratingMapper.toDto(RATING_DRIVER))
                .thenReturn(RATING_DRIVER_RESPONSE);

        RatingResponse checkedRatingResponse = ratingService.getRatingById(RATING_ID);

        verify(ratingRepository).findById(RATING_ID);
        verify(ratingMapper).toDto(RATING_DRIVER);
        assertEquals(RATING_DRIVER_RESPONSE, checkedRatingResponse);
    }

    @Test
    void getRatingById_whenRatingNotExists_throwEntityNotFoundException() {
        when(ratingRepository.findById(RATING_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> ratingService.getRatingById(RATING_ID));
        verify(ratingRepository).findById(RATING_ID);
        verify(ratingMapper, never()).toDto(RATING_DRIVER);
    }

    @Test
    void getRatingByRideAndRole_whenRatingExists_returnRatingResponse() {
        when(ratingRepository.findByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER))
                .thenReturn(Optional.of(RATING_DRIVER));
        when(ratingMapper.toDto(RATING_DRIVER))
                .thenReturn(RATING_DRIVER_RESPONSE);

        RatingResponse actualRatingResponse = ratingService.getRatingByRideIdAndRole(RIDE_ID, DRIVER_ROLE);

        verify(ratingRepository).findByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER);
        verify(ratingMapper).toDto(RATING_DRIVER);
        verify(validator).checkIfExistRide(RIDE_ID);
        assertEquals(RATING_DRIVER_RESPONSE, actualRatingResponse);
    }

    @Test
    void getRatingByRideAndRole_whenRatingNotExists_throwEntityNotFoundException() {
        when(ratingRepository.findByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> ratingService.getRatingByRideIdAndRole(RIDE_ID, DRIVER_ROLE));
        verify(ratingRepository).findByRideIdAndUserRole(RIDE_ID, UserRole.DRIVER);
        verify(validator).checkIfExistRide(RIDE_ID);
        verify(ratingMapper, never()).toDto(RATING_DRIVER);
    }

    @Test
    void getAllRatings_returnRatingContainerResponse() {
        when(ratingRepository.findAll(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RATING_PAGE);
        when(ratingMapper.toDto(RATING_DRIVER))
                .thenReturn(RATING_DRIVER_RESPONSE);
        when(ratingMapper.toDto(RATING_PASSENGER))
                .thenReturn(RATING_PASSENGER_RESPONSE);
        when(ratingContainerResponseMapper.toContainer(RATING_RESPONSE_PAGE))
                .thenReturn(RATING_CONTAINER_RESPONSE);

        RatingContainerResponse actualRatingContainerResponse = ratingService.getAllRatings(OFFSET, LIMIT);

        verify(ratingRepository).findAll(PageRequest.of(OFFSET, LIMIT));
        verify(ratingMapper).toDto(RATING_DRIVER);
        verify(ratingMapper).toDto(RATING_PASSENGER);
        verify(ratingContainerResponseMapper).toContainer(RATING_RESPONSE_PAGE);
        assertEquals(RATING_CONTAINER_RESPONSE, actualRatingContainerResponse);
    }

    @Test
    void getAllByUserIdAndRole_returnRatingContainerResponse() {
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER, PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RATING_DRIVER_PAGE);
        when(ratingMapper.toDto(RATING_DRIVER))
                .thenReturn(RATING_DRIVER_RESPONSE);
        when(ratingContainerResponseMapper.toContainer(RATING_DRIVER_RESPONSE_PAGE))
                .thenReturn(RATING_DRIVER_CONTAINER_RESPONSE);

        RatingContainerResponse actualRatingContainerResponse = ratingService.getAllByUserIdAndRole(USER_ID, DRIVER_ROLE, OFFSET, LIMIT);

        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER, PageRequest.of(OFFSET, LIMIT));
        verify(ratingMapper).toDto(RATING_DRIVER);
        verify(ratingContainerResponseMapper).toContainer(RATING_DRIVER_RESPONSE_PAGE);
        assertEquals(RATING_DRIVER_CONTAINER_RESPONSE, actualRatingContainerResponse);
    }

    @Test
    void getAllByRole_returnRatingContainerResponse() {
        when(ratingRepository.findAllByUserRole(UserRole.DRIVER, PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(RATING_DRIVER_PAGE);
        when(ratingMapper.toDto(RATING_DRIVER))
                .thenReturn(RATING_DRIVER_RESPONSE);
        when(ratingContainerResponseMapper.toContainer(RATING_DRIVER_RESPONSE_PAGE))
                .thenReturn(RATING_DRIVER_CONTAINER_RESPONSE);

        RatingContainerResponse actualRatingContainerResponse = ratingService.getAllByRole(DRIVER_ROLE, OFFSET, LIMIT);

        verify(ratingRepository).findAllByUserRole(UserRole.DRIVER, PageRequest.of(OFFSET, LIMIT));
        verify(ratingMapper).toDto(RATING_DRIVER);
        verify(ratingContainerResponseMapper).toContainer(RATING_DRIVER_RESPONSE_PAGE);
        assertEquals(RATING_DRIVER_CONTAINER_RESPONSE, actualRatingContainerResponse);
    }

    @Test
    void deleteRating_whenRatingExistsAndListRatingsNotEmpty_thenSuccessDelete() {
        when(ratingRepository.findById(RATING_ID))
                .thenReturn(Optional.of(RATING_DRIVER));
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER))
                .thenReturn(RATING_DRIVER_LIST);

        ratingService.deleteRating(RATING_ID);

        verify(ratingRepository).findById(RATING_ID);
        verify(ratingRepository).delete(RATING_DRIVER);
        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }

    @Test
    void deleteRating_whenRatingExistsAndListRatingsEmpty_throwEmptyListException() {
        when(ratingRepository.findById(RATING_ID))
                .thenReturn(Optional.of(RATING_DRIVER));
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER))
                .thenReturn(Collections.emptyList());
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(LIST_EMPTY, RATING_RESOURCE));

        EmptyListException ex = assertThrows(EmptyListException.class,
                () -> ratingService.deleteRating(RATING_ID));
        assertEquals(String.format(LIST_EMPTY, RATING_RESOURCE), ex.getMessage());
        verify(ratingRepository).findById(RATING_ID);
        verify(ratingRepository).delete(RATING_DRIVER);
        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender, never()).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }

    @Test
    void deleteRating_whenRatingNotExists_throwEntityNotFoundException() {
        when(ratingRepository.findById(RATING_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> ratingService.deleteRating(RATING_ID));
        verify(ratingRepository).findById(RATING_ID);
        verify(ratingRepository, never()).delete(RATING_DRIVER);
        verify(ratingRepository, never()).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender, never()).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }

    @Test
    void calculateRating_whenListRatingsNotEmpty_returnAvgRatingResponse() {
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER))
                .thenReturn(RATING_DRIVER_LIST);

        AvgRatingUserResponse actualAvgRatingUserResponse = ratingService.calculateRating(USER_ID, DRIVER_ROLE);

        assertEquals(AVG_RATING_USER_RESPONSE, actualAvgRatingUserResponse);
        verify(validator).checkIfExistUser(USER_ID, UserRole.DRIVER);
        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }

    @Test
    void updateRating_whenRatingExistsAndListRatingEmpty_throwEmptyListException() {
        when(ratingRepository.findById(RATING_ID)).thenReturn(Optional.of(RATING_DRIVER));
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER)).thenReturn(Collections.emptyList());
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).thenReturn(String.format(LIST_EMPTY, RATING_RESOURCE));

        EmptyListException ex = assertThrows(EmptyListException.class,
                () -> ratingService.updateRating(RATING_ID, RATING_UPDATE_DTO));
        assertEquals(String.format(LIST_EMPTY, RATING_RESOURCE), ex.getMessage());
        verify(ratingRepository).findById(RATING_ID);
        verify(ratingRepository).save(RATING_DRIVER);
        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender, never()).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }

    @Test
    void updateRating_whenRatingExistsAndListRatingsNotEmpty_returnRatingResponse() {
        when(ratingRepository.findById(RATING_ID))
                .thenReturn(Optional.of(RATING_DRIVER));
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER))
                .thenReturn(RATING_DRIVER_LIST);
        when(ratingMapper.toDto(RATING_DRIVER))
                .thenReturn(RATING_DRIVER_UPDATED_RESPONSE);

        RatingResponse actualRatingResponse = ratingService.updateRating(RATING_ID, RATING_UPDATE_DTO);

        assertEquals(RATING_DRIVER_UPDATED_RESPONSE, actualRatingResponse);
        verify(ratingRepository).findById(RATING_ID);
        verify(ratingRepository).save(RATING_DRIVER);
        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }

    @Test
    void createRating_whenListRatingsNotEmpty_returnRatingResponse() {
        when(ratingMapper.toEntity(RATING_DRIVER_REQUEST))
                .thenReturn(RATING_DRIVER);
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER))
                .thenReturn(RATING_DRIVER_LIST);
        when(ratingMapper.toDto(RATING_DRIVER))
                .thenReturn(RATING_DRIVER_RESPONSE);

        RatingResponse actualRatingResponse = ratingService.createRating(RATING_DRIVER_REQUEST);

        assertEquals(RATING_DRIVER_RESPONSE, actualRatingResponse);
        verify(validator).checkIfExistUser(USER_ID, UserRole.DRIVER);
        verify(validator).checkIfExistRide(RIDE_ID);
        verify(validator).checkIfExistRatingByRideIdAndRole(RIDE_ID, UserRole.DRIVER);
        verify(ratingRepository).save(RATING_DRIVER);
        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }

    @Test
    void createRating_whenListRatingsEmpty_throwEmptyListException() {
        when(ratingMapper.toEntity(RATING_DRIVER_REQUEST))
                .thenReturn(RATING_DRIVER);
        when(ratingRepository.findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER))
                .thenReturn(Collections.emptyList());
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(LIST_EMPTY, RATING_RESOURCE));

        EmptyListException ex = assertThrows(EmptyListException.class,
                () -> ratingService.createRating(RATING_DRIVER_REQUEST));
        assertEquals(String.format(LIST_EMPTY, RATING_RESOURCE), ex.getMessage());
        verify(validator).checkIfExistUser(USER_ID, UserRole.DRIVER);
        verify(validator).checkIfExistRide(RIDE_ID);
        verify(validator).checkIfExistRatingByRideIdAndRole(RIDE_ID, UserRole.DRIVER);
        verify(ratingRepository).save(RATING_DRIVER);
        verify(ratingRepository).findAllByUserIdAndUserRole(USER_ID, UserRole.DRIVER);
        verify(kafkaSender, never()).sendAvgRatingDriver(AVG_RATING_USER_RESPONSE);
    }
}
