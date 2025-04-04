package cab.aggregator.app.ratingservice.controller;

import cab.aggregator.app.ratingservice.controller.impl.RatingController;
import cab.aggregator.app.ratingservice.exception.EntityNotFoundException;
import cab.aggregator.app.ratingservice.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static cab.aggregator.app.ratingservice.utils.RatingConstants.COUNT_CALLS_METHOD;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.DRIVER_ROLE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.LIMIT;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.OFFSET;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_ID_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_RIDE_ROLE_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_ROLE_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_USER_URL;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_CONTAINER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_CONTAINER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RIDE_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.USER_ID;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_DRIVER_REQUEST;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATING_UPDATE_DTO;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.AVG_RATING_USER_RESPONSE;
import static cab.aggregator.app.ratingservice.utils.RatingConstants.RATINGS_AVG_RATING_URL;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @MockBean
    private RatingService ratingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRatingById_whenRatingExist_returnRatingResponseAndStatusOk() throws Exception {
        when(ratingService.getRatingById(RATING_ID)).thenReturn(RATING_DRIVER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_ID_URL, RATING_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RATING_DRIVER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).getRatingById(RATING_ID);
    }

    @Test
    void getRatingById_whenRatingNotExist_returnStatusNotFound() throws Exception {
        when(ratingService.getRatingById(RATING_ID)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_ID_URL, RATING_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(ratingService, times(COUNT_CALLS_METHOD)).getRatingById(RATING_ID);
    }

    @Test
    void getRatingByRideIdAndRole_whenRatingExist_returnRatingResponseAndStatusOk() throws Exception {
        when(ratingService.getRatingByRideIdAndRole(RIDE_ID, DRIVER_ROLE)).thenReturn(RATING_DRIVER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_RIDE_ROLE_URL, DRIVER_ROLE, RIDE_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RATING_DRIVER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).getRatingByRideIdAndRole(RIDE_ID, DRIVER_ROLE);
    }

    @Test
    void getRatingByRideIdAndRole_whenRatingNotExist_returnStatusNotFound() throws Exception {
        when(ratingService.getRatingByRideIdAndRole(RIDE_ID, DRIVER_ROLE)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_RIDE_ROLE_URL, DRIVER_ROLE, RIDE_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(ratingService, times(COUNT_CALLS_METHOD)).getRatingByRideIdAndRole(RIDE_ID, DRIVER_ROLE);
    }

    @Test
    void getAllRatings_returnRatingContainerResponseAndStatusOk() throws Exception {
        when(ratingService.getAllRatings(OFFSET, LIMIT)).thenReturn(RATING_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_URL);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RATING_CONTAINER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).getAllRatings(OFFSET, LIMIT);
    }

    @Test
    void getAllRatingsByRole_returnRatingContainerResponseAndStatusOk() throws Exception {
        when(ratingService.getAllByRole(DRIVER_ROLE, OFFSET, LIMIT)).thenReturn(RATING_DRIVER_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_ROLE_URL, DRIVER_ROLE);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RATING_DRIVER_CONTAINER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).getAllByRole(DRIVER_ROLE, OFFSET, LIMIT);
    }

    @Test
    void getAllRatingsByUserIdAndRole_returnRatingContainerResponseAndStatusOk() throws Exception {
        when(ratingService.getAllByUserIdAndRole(USER_ID, DRIVER_ROLE, OFFSET, LIMIT)).thenReturn(RATING_DRIVER_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_USER_URL, DRIVER_ROLE, USER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RATING_DRIVER_CONTAINER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).getAllByUserIdAndRole(USER_ID, DRIVER_ROLE, OFFSET, LIMIT);
    }

    @Test
    void deleteRatingById_whenRatingExist_returnStatusOk() throws Exception {
        doNothing().when(ratingService).deleteRating(RATING_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(RATINGS_ID_URL, RATING_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNoContent());
        verify(ratingService, times(COUNT_CALLS_METHOD)).deleteRating(RATING_ID);
    }

    @Test
    void deleteRatingById_whenRatingNotExist_returnStatusNotfound() throws Exception {
        doThrow(EntityNotFoundException.class).when(ratingService).deleteRating(RATING_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(RATINGS_ID_URL, RATING_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(ratingService, times(COUNT_CALLS_METHOD)).deleteRating(RATING_ID);
    }

    @Test
    void createRating_returnRatingResponseAndStatusCreated() throws Exception {
        when(ratingService.createRating(RATING_DRIVER_REQUEST)).thenReturn(RATING_DRIVER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post(RATINGS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RATING_DRIVER_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isCreated())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(RATING_DRIVER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).createRating(RATING_DRIVER_REQUEST);
    }

    @Test
    void updateRating_whenRatingExist_returnRatingResponseAndStatusOk() throws Exception {
        when(ratingService.updateRating(RATING_ID, RATING_UPDATE_DTO)).thenReturn(RATING_DRIVER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = patch(RATINGS_ID_URL, RATING_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RATING_UPDATE_DTO));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(RATING_DRIVER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).updateRating(RATING_ID, RATING_UPDATE_DTO);
    }

    @Test
    void updateRating_whenRatingNotExist_returnStatusNotFound() throws Exception {
        when(ratingService.updateRating(RATING_ID, RATING_UPDATE_DTO)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = patch(RATINGS_ID_URL, RATING_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RATING_UPDATE_DTO));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(ratingService, times(COUNT_CALLS_METHOD)).updateRating(RATING_ID, RATING_UPDATE_DTO);
    }

    @Test
    void calculateAvgRatingUser_returnAvgRatingResponseAndStatusOk() throws Exception {
        when(ratingService.calculateRating(USER_ID, DRIVER_ROLE)).thenReturn(AVG_RATING_USER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RATINGS_AVG_RATING_URL, DRIVER_ROLE, USER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(AVG_RATING_USER_RESPONSE)));
        verify(ratingService, times(COUNT_CALLS_METHOD)).calculateRating(USER_ID, DRIVER_ROLE);
    }

}
