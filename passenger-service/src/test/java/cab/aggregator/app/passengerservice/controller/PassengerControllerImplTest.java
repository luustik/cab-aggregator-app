package cab.aggregator.app.passengerservice.controller;

import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.exception.EntityNotFoundException;
import cab.aggregator.app.passengerservice.service.PassengerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static cab.aggregator.app.passengerservice.utils.PassengerConstants.COUNT_CALLS_METHOD;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.LIMIT;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.OFFSET;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_ADMIN_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_EMAIL_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_ID_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_PHONE_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_CONTAINER_RESPONSE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_EMAIL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_ID;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_INVALID_EMAIL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_INVALID_PHONE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_PHONE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_RESPONSE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGERS_SAFE_ID_URL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_INVALID_REQUEST;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PassengerControllerImpl.class)
class PassengerControllerImplTest {

    @MockBean
    private PassengerService passengerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPassengerById_whenPassengerExist_returnPassengerResponseAndStatusOk() throws Exception {
        when(passengerService.getPassengerById(PASSENGER_ID)).thenReturn(PASSENGER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_ID_URL, PASSENGER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(PASSENGER_RESPONSE)));
        verify(passengerService, times(COUNT_CALLS_METHOD)).getPassengerById(PASSENGER_ID);
    }

    @Test
    void getPassengerById_whenPassengerNotExist_returnStatusNotFound() throws Exception {
        when(passengerService.getPassengerById(PASSENGER_ID)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_ID_URL, PASSENGER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(passengerService, times(COUNT_CALLS_METHOD)).getPassengerById(PASSENGER_ID);
    }

    @Test
    void getPassengerByPhone_whenPassengerExist_returnPassengerResponseAndStatusOk() throws Exception {
        when(passengerService.getPassengerByPhone(PASSENGER_PHONE)).thenReturn(PASSENGER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_PHONE_URL, PASSENGER_PHONE);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(PASSENGER_RESPONSE)));
        verify(passengerService, times(COUNT_CALLS_METHOD)).getPassengerByPhone(PASSENGER_PHONE);
    }

    @Test
    void getPassengerByPhone_whenPassengerNotExist_returnStatusNotFound() throws Exception {
        when(passengerService.getPassengerByPhone(PASSENGER_PHONE)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_PHONE_URL, PASSENGER_PHONE);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(passengerService, times(COUNT_CALLS_METHOD)).getPassengerByPhone(PASSENGER_PHONE);
    }

    @Test
    void getPassengerByPhone_whenPhoneNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_PHONE_URL, PASSENGER_INVALID_PHONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PASSENGER_INVALID_PHONE));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(passengerService, never()).getPassengerByPhone(anyString());
    }

    @Test
    void getPassengerByEmail_whenPassengerExist_returnPassengerResponseAndStatusOk() throws Exception {
        when(passengerService.getPassengerByEmail(PASSENGER_EMAIL)).thenReturn(PASSENGER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_EMAIL_URL, PASSENGER_EMAIL);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(PASSENGER_RESPONSE)));
        verify(passengerService, times(COUNT_CALLS_METHOD)).getPassengerByEmail(PASSENGER_EMAIL);
    }

    @Test
    void getPassengerByEmail_whenPassengerNotExist_returnStatusNotFound() throws Exception {
        when(passengerService.getPassengerByEmail(PASSENGER_EMAIL)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_EMAIL_URL, PASSENGER_EMAIL);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(passengerService, times(COUNT_CALLS_METHOD)).getPassengerByEmail(PASSENGER_EMAIL);
    }

    @Test
    void getPassengerByEmail_whenEmailNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_EMAIL_URL, PASSENGER_INVALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PASSENGER_INVALID_EMAIL));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(passengerService, never()).getPassengerByEmail(anyString());
    }

    @Test
    void getAllPassengersAdmin_returnPassengerContainerResponseAndStatusOk() throws Exception {
        when(passengerService.getAllPassengersAdmin(OFFSET, LIMIT)).thenReturn(PASSENGER_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_ADMIN_URL);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(PASSENGER_CONTAINER_RESPONSE)));
        verify(passengerService, times(COUNT_CALLS_METHOD)).getAllPassengersAdmin(OFFSET, LIMIT);
    }

    @Test
    void getAllPassengers_returnPassengerContainerResponseAndStatusOk() throws Exception {
        when(passengerService.getAllPassengers(OFFSET, LIMIT)).thenReturn(PASSENGER_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(PASSENGERS_URL);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(PASSENGER_CONTAINER_RESPONSE)));
        verify(passengerService, times(COUNT_CALLS_METHOD)).getAllPassengers(OFFSET, LIMIT);
    }

    @Test
    void softDeletePassengerById_whenPassengerExist_returnStatusOk() throws Exception {
        doNothing().when(passengerService).softDeletePassenger(PASSENGER_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(PASSENGERS_SAFE_ID_URL, PASSENGER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk());
        verify(passengerService, times(COUNT_CALLS_METHOD)).softDeletePassenger(PASSENGER_ID);
    }

    @Test
    void softDeletePassengerById_whenPassengerNotExist_returnStatusNotfound() throws Exception {
        doThrow(EntityNotFoundException.class).when(passengerService).softDeletePassenger(PASSENGER_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(PASSENGERS_SAFE_ID_URL, PASSENGER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(passengerService, times(COUNT_CALLS_METHOD)).softDeletePassenger(PASSENGER_ID);
    }

    @Test
    void hardDeletePassengerById_whenPassengerExist_returnStatusOk() throws Exception {
        doNothing().when(passengerService).hardDeletePassenger(PASSENGER_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(PASSENGERS_ID_URL, PASSENGER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk());
        verify(passengerService, times(COUNT_CALLS_METHOD)).hardDeletePassenger(PASSENGER_ID);
    }

    @Test
    void hardDeletePassengerById_whenPassengerNotExist_returnStatusNotfound() throws Exception {
        doThrow(EntityNotFoundException.class).when(passengerService).hardDeletePassenger(PASSENGER_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(PASSENGERS_ID_URL, PASSENGER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(passengerService, times(COUNT_CALLS_METHOD)).hardDeletePassenger(PASSENGER_ID);
    }

    @Test
    void createPassenger_whenPassengerRequestValid_returnPassengerResponseAndStatusCreated() throws Exception {
        when(passengerService.createPassenger(PASSENGER_REQUEST)).thenReturn(PASSENGER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post(PASSENGERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PASSENGER_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isCreated())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(PASSENGER_RESPONSE)));
        verify(passengerService, times(COUNT_CALLS_METHOD)).createPassenger(PASSENGER_REQUEST);
    }

    @Test
    void createPassenger_whenPassengerEmailAndPhoneNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post(PASSENGERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PASSENGER_INVALID_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(passengerService, never()).createPassenger(any(PassengerRequest.class));
    }

    @Test
    void updatePassenger_whenPassengerExistAndPassengerRequestValid_returnPassengerResponseAndStatusOk() throws Exception {
        when(passengerService.updatePassenger(PASSENGER_ID, PASSENGER_REQUEST)).thenReturn(PASSENGER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(PASSENGERS_ID_URL, PASSENGER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PASSENGER_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(PASSENGER_RESPONSE)));
        verify(passengerService, times(COUNT_CALLS_METHOD)).updatePassenger(PASSENGER_ID, PASSENGER_REQUEST);
    }

    @Test
    void updatePassenger_whenPassengerNotExistAndPassengerRequestValid_returnStatusNotFound() throws Exception {
        when(passengerService.updatePassenger(PASSENGER_ID, PASSENGER_REQUEST)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(PASSENGERS_ID_URL, PASSENGER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PASSENGER_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(passengerService, times(COUNT_CALLS_METHOD)).updatePassenger(PASSENGER_ID, PASSENGER_REQUEST);
    }

    @Test
    void updatePassenger_whenPassengerEmailAndPhoneNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(PASSENGERS_ID_URL, PASSENGER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PASSENGER_INVALID_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(passengerService, never()).updatePassenger(anyInt(), any(PassengerRequest.class));
    }
}
