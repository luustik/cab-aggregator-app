package cab.aggregator.app.rideservice.controller;

import cab.aggregator.app.rideservice.exception.EntityNotFoundException;
import cab.aggregator.app.rideservice.service.RideService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static cab.aggregator.app.rideservice.utils.RideConstants.COUNT_CALLS_METHOD;
import static cab.aggregator.app.rideservice.utils.RideConstants.DRIVER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.LIMIT;
import static cab.aggregator.app.rideservice.utils.RideConstants.OFFSET;
import static cab.aggregator.app.rideservice.utils.RideConstants.PASSENGER_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_DATE_TIME_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_DRIVER_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_ID_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_PASSENGER_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_STATUS_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDES_URL;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_CONTAINER_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_END_RANGE_TIME_INVALID_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_END_RANGE_TIME_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_ID;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_INVALID_STATUS;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_REQUEST;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_RESPONSE;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_START_RANGE_TIME_INVALID_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_START_RANGE_TIME_STR;
import static cab.aggregator.app.rideservice.utils.RideConstants.RIDE_STATUS;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(RideControllerImpl.class)
class RideControllerImplTest {

    @MockBean
    private RideService rideService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRideById_whenRideExist_returnRideResponseAndStatusOk() throws Exception {
        when(rideService.getRideById(RIDE_ID)).thenReturn(RIDE_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_ID_URL, RIDE_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RIDE_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).getRideById(RIDE_ID);
    }

    @Test
    void getRideById_whenRideNotExist_returnStatusNotFound() throws Exception {
        when(rideService.getRideById(RIDE_ID)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_ID_URL, RIDE_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(rideService, times(COUNT_CALLS_METHOD)).getRideById(RIDE_ID);
    }

    @Test
    void getAllRides_returnRideContainerResponseAndStatusOk() throws Exception {
        when(rideService.getAllRides(OFFSET, LIMIT)).thenReturn(RIDE_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_URL);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RIDE_CONTAINER_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).getAllRides(OFFSET, LIMIT);
    }

    @Test
    void getAllRidesByDriverId_returnRideContainerResponseAndStatusOk() throws Exception {
        when(rideService.getAllRidesByDriverId(DRIVER_ID, OFFSET, LIMIT)).thenReturn(RIDE_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_DRIVER_URL, DRIVER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RIDE_CONTAINER_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).getAllRidesByDriverId(DRIVER_ID, OFFSET, LIMIT);
    }

    @Test
    void getAllRidesByPassengerId_returnRideContainerResponseAndStatusOk() throws Exception {
        when(rideService.getAllRidesByPassengerId(PASSENGER_ID, OFFSET, LIMIT)).thenReturn(RIDE_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_PASSENGER_URL, PASSENGER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RIDE_CONTAINER_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).getAllRidesByPassengerId(PASSENGER_ID, OFFSET, LIMIT);
    }

    @Test
    void getAllRidesByStatus_whenStatusValid_returnRideContainerResponseAndStatusOk() throws Exception {
        when(rideService.getAllRidesByStatus(RIDE_STATUS, OFFSET, LIMIT)).thenReturn(RIDE_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_STATUS_URL, RIDE_STATUS);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RIDE_CONTAINER_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).getAllRidesByStatus(RIDE_STATUS, OFFSET, LIMIT);
    }

    @Test
    void getAllRidesByStatus_whenStatusNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_STATUS_URL, RIDE_INVALID_STATUS);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(rideService, never()).getAllRidesByStatus(anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllBetweenOrderDateTime_whenDateTimeValid_returnRideContainerResponseAndStatusOk() throws Exception {
        when(rideService.getAllBetweenOrderDateTime(RIDE_START_RANGE_TIME_STR, RIDE_END_RANGE_TIME_STR, OFFSET, LIMIT))
                .thenReturn(RIDE_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_DATE_TIME_URL)
                .param("start", RIDE_START_RANGE_TIME_STR)
                .param("end", RIDE_END_RANGE_TIME_STR);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(RIDE_CONTAINER_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).getAllBetweenOrderDateTime(RIDE_START_RANGE_TIME_STR, RIDE_END_RANGE_TIME_STR, OFFSET, LIMIT);
    }

    @Test
    void getAllBetweenOrderDateTime_whenDateTimeNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(RIDES_DATE_TIME_URL)
                .param("start", RIDE_START_RANGE_TIME_INVALID_STR)
                .param("end", RIDE_END_RANGE_TIME_INVALID_STR);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(rideService, never()).getAllBetweenOrderDateTime(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void deleteRideById_whenRideExist_returnStatusOk() throws Exception {
        doNothing().when(rideService).deleteRide(RIDE_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(RIDES_ID_URL, RIDE_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk());
        verify(rideService, times(COUNT_CALLS_METHOD)).deleteRide(RIDE_ID);
    }

    @Test
    void deleteRideById_whenRideNotExist_returnStatusNotfound() throws Exception {
        doThrow(EntityNotFoundException.class).when(rideService).deleteRide(RIDE_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(RIDES_ID_URL, RIDE_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(rideService, times(COUNT_CALLS_METHOD)).deleteRide(RIDE_ID);
    }

    @Test
    void createRide_returnRideResponseAndStatusCreated() throws Exception {
        when(rideService.createRide(RIDE_REQUEST)).thenReturn(RIDE_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post(RIDES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RIDE_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isCreated())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(RIDE_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).createRide(RIDE_REQUEST);
    }

    @Test
    void updateRide_whenRideExist_returnRideResponseAndStatusOk() throws Exception {
        when(rideService.updateRide(RIDE_ID, RIDE_REQUEST)).thenReturn(RIDE_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(RIDES_ID_URL, RIDE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RIDE_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(RIDE_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).updateRide(RIDE_ID, RIDE_REQUEST);
    }

    @Test
    void updateRide_whenRideNotExist_returnStatusNotFound() throws Exception {
        when(rideService.updateRide(RIDE_ID, RIDE_REQUEST)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(RIDES_ID_URL, RIDE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RIDE_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(rideService, times(COUNT_CALLS_METHOD)).updateRide(RIDE_ID, RIDE_REQUEST);
    }

    @Test
    void updateRideStatus_whenRideExistAndRideStatusValid_returnRideResponseAndStatusOk() throws Exception {
        when(rideService.updateRideStatus(RIDE_ID, RIDE_STATUS)).thenReturn(RIDE_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = patch(RIDES_ID_URL, RIDE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(RIDE_STATUS);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(RIDE_RESPONSE)));
        verify(rideService, times(COUNT_CALLS_METHOD)).updateRideStatus(RIDE_ID, RIDE_STATUS);
    }

    @Test
    void updateRideStatus_whenRideNotExistAndRideStatusValid_returnStatusNotFound() throws Exception {
        when(rideService.updateRideStatus(RIDE_ID, RIDE_STATUS)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = patch(RIDES_ID_URL, RIDE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(RIDE_STATUS);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(rideService, times(COUNT_CALLS_METHOD)).updateRideStatus(RIDE_ID, RIDE_STATUS);
    }

    @Test
    void updateRideStatus_whenRideStatusNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = patch(RIDES_ID_URL, RIDE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(RIDE_INVALID_STATUS);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(rideService, never()).updateRideStatus(anyLong(), anyString());
    }
}
