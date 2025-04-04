package cab.aggregator.app.driverservice.controller;

import cab.aggregator.app.driverservice.dto.request.CarRequest;
import cab.aggregator.app.driverservice.exception.EntityNotFoundException;
import cab.aggregator.app.driverservice.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_BY_NUMBER_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_DRIVER_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_ID_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CARS_URL;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_CONTAINER_RESPONSE;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_ID;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_INVALID_REQUEST;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_NUMBER;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_REQUEST;
import static cab.aggregator.app.driverservice.utils.CarConstants.CAR_RESPONSE;
import static cab.aggregator.app.driverservice.utils.CarConstants.COUNT_CALLS_METHOD;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_ID;
import static cab.aggregator.app.driverservice.utils.DriverConstants.LIMIT;
import static cab.aggregator.app.driverservice.utils.DriverConstants.OFFSET;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@WebMvcTest(CarController.class)
class CarControllerTest {

    @MockBean
    private CarService carService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCarById_whenCarExist_returnCarResponseAndStatusOk() throws Exception {
        when(carService.getCarById(CAR_ID)).thenReturn(CAR_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(CARS_ID_URL, CAR_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(CAR_RESPONSE)));
        verify(carService, times(COUNT_CALLS_METHOD)).getCarById(CAR_ID);
    }

    @Test
    void getCarById_whenCarNotExist_returnStatusNotFound() throws Exception {
        when(carService.getCarById(CAR_ID)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(CARS_ID_URL, CAR_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(carService, times(COUNT_CALLS_METHOD)).getCarById(CAR_ID);
    }

    @Test
    void getAllCars_returnCarContainerResponseAndStatusOk() throws Exception {
        when(carService.getAllCars(OFFSET, LIMIT)).thenReturn(CAR_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(CARS_URL);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(CAR_CONTAINER_RESPONSE)));
        verify(carService, times(COUNT_CALLS_METHOD)).getAllCars(OFFSET, LIMIT);
    }

    @Test
    void getCarByCarNumber_whenCarExist_returnCarResponseAndStatusOk() throws Exception {
        when(carService.getCarByCarNumber(CAR_NUMBER)).thenReturn(CAR_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(CARS_BY_NUMBER_URL, CAR_NUMBER);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(CAR_RESPONSE)));
        verify(carService, times(COUNT_CALLS_METHOD)).getCarByCarNumber(CAR_NUMBER);
    }

    @Test
    void getCarByCarNumber_whenCarNotExist_returnStatusNotFound() throws Exception {
        when(carService.getCarByCarNumber(CAR_NUMBER)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(CARS_BY_NUMBER_URL, CAR_NUMBER);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(carService, times(COUNT_CALLS_METHOD)).getCarByCarNumber(CAR_NUMBER);
    }

    @Test
    void getAllCarsByDriverId_whenDriverExist_returnCarResponseAndStatusOk() throws Exception {
        when(carService.getAllCarsByDriverId(DRIVER_ID, OFFSET, LIMIT)).thenReturn(CAR_CONTAINER_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(CARS_DRIVER_URL, DRIVER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(CAR_CONTAINER_RESPONSE)));
        verify(carService, times(COUNT_CALLS_METHOD)).getAllCarsByDriverId(DRIVER_ID, OFFSET, LIMIT);
    }

    @Test
    void getAllCarsByDriverId_whenDriverNotExist_returnStatusNotFound() throws Exception {
        when(carService.getAllCarsByDriverId(DRIVER_ID, OFFSET, LIMIT)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(CARS_DRIVER_URL, DRIVER_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(carService, times(COUNT_CALLS_METHOD)).getAllCarsByDriverId(DRIVER_ID, OFFSET, LIMIT);
    }

    @Test
    void deleteCar_whenCarExist_returnStatusOk() throws Exception {
        doNothing().when(carService).deleteCar(CAR_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(CARS_ID_URL, CAR_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk());
        verify(carService, times(COUNT_CALLS_METHOD)).deleteCar(CAR_ID);
    }

    @Test
    void deleteCar_whenCarNotExist_returnStatusNotfound() throws Exception {
        doThrow(EntityNotFoundException.class).when(carService).deleteCar(CAR_ID);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete(CARS_ID_URL, CAR_ID);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(carService, times(COUNT_CALLS_METHOD)).deleteCar(CAR_ID);
    }

    @Test
    void createCar_whenCarRequestValid_returnCarResponseAndStatusCreated() throws Exception {
        when(carService.createCar(CAR_REQUEST)).thenReturn(CAR_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post(CARS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CAR_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isCreated())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(CAR_RESPONSE)));
        verify(carService, times(COUNT_CALLS_METHOD)).createCar(CAR_REQUEST);
    }

    @Test
    void createCar_whenCarNumberNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post(CARS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CAR_INVALID_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(carService, never()).createCar(any());
    }

    @Test
    void updateCar_whenCarExistAndCarRequestValid_returnCarResponseAndStatusOk() throws Exception {
        when(carService.updateCar(CAR_ID, CAR_REQUEST)).thenReturn(CAR_RESPONSE);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(CARS_ID_URL, CAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CAR_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(CAR_RESPONSE)));
        verify(carService, times(COUNT_CALLS_METHOD)).updateCar(CAR_ID, CAR_REQUEST);
    }

    @Test
    void updateCar_whenCarNotExistAndCarRequestValid_returnStatusNotFound() throws Exception {
        when(carService.updateCar(CAR_ID, CAR_REQUEST)).thenThrow(EntityNotFoundException.class);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(CARS_ID_URL, CAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CAR_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isNotFound());
        verify(carService, times(COUNT_CALLS_METHOD)).updateCar(CAR_ID, CAR_REQUEST);
    }

    @Test
    void updateCar_whenCarNumberNotValid_returnStatusBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put(CARS_ID_URL, CAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CAR_INVALID_REQUEST));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status()
                        .isBadRequest());
        verify(carService, never()).updateCar(anyInt(), any(CarRequest.class));
    }
}
