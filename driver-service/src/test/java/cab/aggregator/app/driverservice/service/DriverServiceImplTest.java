package cab.aggregator.app.driverservice.service;

import cab.aggregator.app.driverservice.dto.response.DriverContainerResponse;
import cab.aggregator.app.driverservice.dto.response.DriverResponse;
import cab.aggregator.app.driverservice.entity.enums.Gender;
import cab.aggregator.app.driverservice.exception.EntityNotFoundException;
import cab.aggregator.app.driverservice.exception.ResourceAlreadyExistsException;
import cab.aggregator.app.driverservice.mapper.DriverContainerResponseMapper;
import cab.aggregator.app.driverservice.mapper.DriverMapper;
import cab.aggregator.app.driverservice.repository.DriverRepository;
import cab.aggregator.app.driverservice.service.impl.DriverServiceImpl;
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

import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_CONTAINER_RESPONSE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_EMAIL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_GENDER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_ID;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_LIST;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_PAGE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_REQUEST;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_RESOURCE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_RESPONSE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_RESPONSE_PAGE;
import static cab.aggregator.app.driverservice.utils.DriverConstants.LIMIT;
import static cab.aggregator.app.driverservice.utils.DriverConstants.OFFSET;
import static cab.aggregator.app.driverservice.utils.DriverConstants.RESOURCE_ALREADY_EXISTS;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_PHONE_NUMBER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_UPDATED_PHONE_NUMBER;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_UPDATED_EMAIL;
import static cab.aggregator.app.driverservice.utils.DriverConstants.DRIVER_UPDATED_REQUEST;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {
    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private DriverContainerResponseMapper driverContainerResponseMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private DriverServiceImpl driverService;

    @Test
    void getDriverById_whenDriverExists_returnDriverResponse() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);

        DriverResponse checkedDriverResponse = driverService.getDriverById(DRIVER_ID);

        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverMapper).toDto(DRIVER);
        assertEquals(DRIVER_RESPONSE, checkedDriverResponse);
    }

    @Test
    void getDriverById_whenDriverNotExists_throwEntityNotFoundException() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> driverService.getDriverById(DRIVER_ID));
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
    }

    @Test
    void getAllDriversAdmin_returnDriverContainerResponse() {
        when(driverRepository.findAll(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(DRIVER_PAGE);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);
        when(driverContainerResponseMapper.toContainer(DRIVER_RESPONSE_PAGE))
                .thenReturn(DRIVER_CONTAINER_RESPONSE);

        DriverContainerResponse actualDriverContainerResponse = driverService.getAllDriversAdmin(OFFSET, LIMIT);

        verify(driverRepository).findAll(PageRequest.of(OFFSET, LIMIT));
        verify(driverMapper).toDto(DRIVER);
        verify(driverContainerResponseMapper).toContainer(DRIVER_RESPONSE_PAGE);
        assertEquals(DRIVER_CONTAINER_RESPONSE, actualDriverContainerResponse);
    }

    @Test
    void getAllDrivers_returnDriverContainerResponse() {
        when(driverRepository.findByDeletedFalse(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(DRIVER_PAGE);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);
        when(driverContainerResponseMapper.toContainer(DRIVER_RESPONSE_PAGE))
                .thenReturn(DRIVER_CONTAINER_RESPONSE);

        DriverContainerResponse actualDriverContainerResponse = driverService.getAllDrivers(OFFSET, LIMIT);

        verify(driverRepository).findByDeletedFalse(PageRequest.of(OFFSET, LIMIT));
        verify(driverMapper).toDto(DRIVER);
        verify(driverContainerResponseMapper).toContainer(DRIVER_RESPONSE_PAGE);
        assertEquals(DRIVER_CONTAINER_RESPONSE, actualDriverContainerResponse);
    }

    @Test
    void getDriversByGender_returnDriverContainerResponse() {
        when(driverRepository.findAllByGenderAndDeletedFalse(Gender.valueOf(DRIVER_GENDER), PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(DRIVER_PAGE);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);
        when(driverContainerResponseMapper.toContainer(DRIVER_RESPONSE_PAGE))
                .thenReturn(DRIVER_CONTAINER_RESPONSE);

        DriverContainerResponse actualDriverContainerResponse = driverService.getDriversByGender(DRIVER_GENDER, OFFSET, LIMIT);

        verify(driverRepository).findAllByGenderAndDeletedFalse(Gender.valueOf(DRIVER_GENDER), PageRequest.of(OFFSET, LIMIT));
        verify(driverMapper).toDto(DRIVER);
        verify(driverContainerResponseMapper).toContainer(DRIVER_RESPONSE_PAGE);
        assertEquals(DRIVER_CONTAINER_RESPONSE, actualDriverContainerResponse);
    }

    @Test
    void safeDeleteDriver_whenDriverExists_thenSetDeletedToDriver() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(driverRepository.save(DRIVER))
                .thenReturn(DRIVER);

        driverService.safeDeleteDriver(DRIVER_ID);

        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository).save(DRIVER);
    }

    @Test
    void safeDeleteDriver_whenDriverNotExists_throwEntityNotFoundException() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> driverService.safeDeleteDriver(DRIVER_ID));
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository, never()).save(DRIVER);
    }

    @Test
    void deleteDriver_whenDriverExists_thenSuccessDelete() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));

        driverService.deleteDriver(DRIVER_ID);

        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository).delete(DRIVER);
    }

    @Test
    void deleteDriver_whenDriverNotExists_throwEntityNotFoundException() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> driverService.deleteDriver(DRIVER_ID));
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository, never()).delete(DRIVER);
    }

    @Test
    void createDriver_whenDriverDeleted_thenReturnDriverResponse() {
        when(driverRepository.findByDeletedTrue())
                .thenReturn(DRIVER_LIST);
        when(driverRepository.save(DRIVER))
                .thenReturn(DRIVER);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);

        DriverResponse actualDriverResponse = driverService.createDriver(DRIVER_REQUEST);

        assertEquals(DRIVER_RESPONSE, actualDriverResponse);
        verify(driverRepository).findByDeletedTrue();
        verify(driverRepository).save(DRIVER);
        verify(driverMapper).toDto(DRIVER);
        verify(driverMapper, never()).toEntity(DRIVER_REQUEST);
    }

    @Test
    void createDriver_whenDriverNotDeletedAndEmailExists_throwResourceAlreadyExistsException() {
        when(driverRepository.findByDeletedTrue())
                .thenReturn(Collections.emptyList());
        when(driverRepository.existsByEmail(DRIVER_EMAIL))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_EMAIL));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> driverService.createDriver(DRIVER_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_EMAIL), ex.getMessage());

        verify(driverRepository).findByDeletedTrue();
        verify(driverRepository).existsByEmail(DRIVER_EMAIL);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(driverRepository, never()).save(DRIVER);
    }

    @Test
    void createDriver_whenDriverNotDeletedAndEmailNotExistsAndPhoneNumberExists_throwResourceAlreadyExistsException() {
        when(driverRepository.findByDeletedTrue())
                .thenReturn(Collections.emptyList());
        when(driverRepository.existsByEmail(DRIVER_EMAIL))
                .thenReturn(FALSE);
        when(driverRepository.existsByPhoneNumber(DRIVER_PHONE_NUMBER))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_PHONE_NUMBER));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> driverService.createDriver(DRIVER_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_PHONE_NUMBER), ex.getMessage());
        verify(driverRepository).findByDeletedTrue();
        verify(driverRepository).existsByEmail(DRIVER_EMAIL);
        verify(driverRepository).existsByPhoneNumber(DRIVER_PHONE_NUMBER);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(driverRepository, never()).save(DRIVER);
    }

    @Test
    void createDriver_whenDriverNotDeletedAndEmailNotExistsAndPhoneNumberNotExists_returnDriverResponse() {
        when(driverRepository.findByDeletedTrue())
                .thenReturn(Collections.emptyList());
        when(driverRepository.existsByEmail(DRIVER_EMAIL))
                .thenReturn(FALSE);
        when(driverRepository.existsByPhoneNumber(DRIVER_PHONE_NUMBER))
                .thenReturn(FALSE);
        when(driverMapper.toEntity(DRIVER_REQUEST))
                .thenReturn(DRIVER);
        when(driverRepository.save(DRIVER))
                .thenReturn(DRIVER);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);

        DriverResponse actualDriverResponse = driverService.createDriver(DRIVER_REQUEST);

        assertEquals(DRIVER_RESPONSE, actualDriverResponse);
        verify(driverRepository).findByDeletedTrue();
        verify(driverRepository).existsByEmail(DRIVER_EMAIL);
        verify(driverRepository).existsByPhoneNumber(DRIVER_PHONE_NUMBER);
        verify(driverMapper).toEntity(DRIVER_REQUEST);
        verify(driverRepository).save(DRIVER);
        verify(driverMapper).toDto(DRIVER);
    }

    @Test
    void updateDriver_whenDriverNotFound_throwEntityNotFoundException() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> driverService.updateDriver(DRIVER_ID, DRIVER_REQUEST));
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository, never()).save(DRIVER);
    }

    @Test
    void updateDriver_whenDriverExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailExists_throwResourceAlreadyExistsException() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(driverRepository.existsByEmail(DRIVER_UPDATED_EMAIL))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_UPDATED_EMAIL));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> driverService.updateDriver(DRIVER_ID, DRIVER_UPDATED_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_UPDATED_EMAIL), ex.getMessage());
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository).existsByEmail(DRIVER_UPDATED_EMAIL);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(driverRepository, never()).save(DRIVER);
    }

    @Test
    void updateDriver_whenDriverExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailNotExistsAndCurrentPhoneIsNotUpdatedPhoneAndUpdatedPhoneExists_throwResourceAlreadyExistsException() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(driverRepository.existsByEmail(DRIVER_UPDATED_EMAIL))
                .thenReturn(FALSE);
        when(driverRepository.existsByPhoneNumber(DRIVER_UPDATED_PHONE_NUMBER))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_UPDATED_PHONE_NUMBER));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> driverService.updateDriver(DRIVER_ID, DRIVER_UPDATED_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, DRIVER_RESOURCE, DRIVER_UPDATED_PHONE_NUMBER), ex.getMessage());
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository).existsByEmail(DRIVER_UPDATED_EMAIL);
        verify(driverRepository).existsByPhoneNumber(DRIVER_UPDATED_PHONE_NUMBER);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(driverRepository, never()).save(DRIVER);
    }

    @Test
    void updateDriver_whenDriverExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailNotExistsAndCurrentPhoneIsNotUpdatedPhoneAndUpdatedPhoneNotExists_returnDriverResponse() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(driverRepository.existsByEmail(DRIVER_UPDATED_EMAIL))
                .thenReturn(FALSE);
        when(driverRepository.existsByPhoneNumber(DRIVER_UPDATED_PHONE_NUMBER))
                .thenReturn(FALSE);
        when(driverRepository.save(DRIVER))
                .thenReturn(DRIVER);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);

        DriverResponse actualDriverResponse = driverService.updateDriver(DRIVER_ID, DRIVER_UPDATED_REQUEST);

        assertEquals(DRIVER_RESPONSE, actualDriverResponse);
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository).existsByEmail(DRIVER_UPDATED_EMAIL);
        verify(driverRepository).existsByPhoneNumber(DRIVER_UPDATED_PHONE_NUMBER);
        verify(driverRepository).save(DRIVER);
        verify(driverMapper).toDto(DRIVER);
    }

    @Test
    void updateDriver_whenDriverExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailNotExistsAndCurrentPhoneIsUpdatedPhone_returnDriverResponse() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(driverRepository.existsByEmail(DRIVER_UPDATED_EMAIL))
                .thenReturn(FALSE);
        when(driverRepository.save(DRIVER))
                .thenReturn(DRIVER);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);

        DriverResponse actualDriverResponse = driverService.updateDriver(DRIVER_ID, DRIVER_UPDATED_REQUEST);

        assertEquals(DRIVER_RESPONSE, actualDriverResponse);
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository).existsByEmail(DRIVER_UPDATED_EMAIL);
        verify(driverRepository).save(DRIVER);
        verify(driverMapper).toDto(DRIVER);
    }

    @Test
    void updateDriver_whenDriverExistsAndCurrentEmailIsUpdatedEmailAndCurrentPhoneIsUpdatedPhone_returnDriverResponse() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID))
                .thenReturn(Optional.of(DRIVER));
        when(driverRepository.save(DRIVER))
                .thenReturn(DRIVER);
        when(driverMapper.toDto(DRIVER))
                .thenReturn(DRIVER_RESPONSE);

        DriverResponse actualDriverResponse = driverService.updateDriver(DRIVER_ID, DRIVER_REQUEST);

        assertEquals(DRIVER_RESPONSE, actualDriverResponse);
        verify(driverRepository).findByIdAndDeletedFalse(DRIVER_ID);
        verify(driverRepository).save(DRIVER);
        verify(driverMapper).toDto(DRIVER);
    }

}
