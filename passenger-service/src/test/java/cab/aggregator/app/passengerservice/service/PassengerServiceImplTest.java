package cab.aggregator.app.passengerservice.service;

import cab.aggregator.app.passengerservice.dto.response.PassengerContainerResponse;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import cab.aggregator.app.passengerservice.exception.EntityNotFoundException;
import cab.aggregator.app.passengerservice.exception.ResourceAlreadyExistsException;
import cab.aggregator.app.passengerservice.mapper.PassengerContainerMapper;
import cab.aggregator.app.passengerservice.mapper.PassengerMapper;
import cab.aggregator.app.passengerservice.repository.PassengerRepository;
import cab.aggregator.app.passengerservice.service.impl.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;

import java.util.Locale;
import java.util.Optional;

import static cab.aggregator.app.passengerservice.utils.PassengerConstants.LIMIT;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.OFFSET;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_CONTAINER_RESPONSE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_EMAIL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_ID;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_PAGE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_PHONE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_REQUEST;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_RESOURCE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_RESPONSE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_RESPONSE_PAGE;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_UPDATED_EMAIL;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.RESOURCE_ALREADY_EXISTS;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_UPDATED_REQUEST;
import static cab.aggregator.app.passengerservice.utils.PassengerConstants.PASSENGER_UPDATED_PHONE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {
    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PassengerContainerMapper passengerContainerResponseMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void getPassengerById_whenPassengerExists_returnPassengerResponse() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse checkedPassengerResponse = passengerService.getPassengerById(PASSENGER_ID);

        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerMapper).toDto(PASSENGER);
        assertEquals(PASSENGER_RESPONSE, checkedPassengerResponse);
    }

    @Test
    void getPassengerById_whenPassengerNotExists_throwEntityNotFoundException() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> passengerService.getPassengerById(PASSENGER_ID));
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
    }

    @Test
    void getAllPassengersAdmin_returnPassengerContainerResponse() {
        when(passengerRepository.findAll(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(PASSENGER_PAGE);
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);
        when(passengerContainerResponseMapper.toContainer(PASSENGER_RESPONSE_PAGE))
                .thenReturn(PASSENGER_CONTAINER_RESPONSE);

        PassengerContainerResponse actualPassengerContainerResponse = passengerService.getAllPassengersAdmin(OFFSET, LIMIT);

        verify(passengerRepository).findAll(PageRequest.of(OFFSET, LIMIT));
        verify(passengerMapper).toDto(PASSENGER);
        verify(passengerContainerResponseMapper).toContainer(PASSENGER_RESPONSE_PAGE);
        assertEquals(PASSENGER_CONTAINER_RESPONSE, actualPassengerContainerResponse);
    }

    @Test
    void getAllPassenger_returnPassengerContainerResponse() {
        when(passengerRepository.findByDeletedFalse(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(PASSENGER_PAGE);
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);
        when(passengerContainerResponseMapper.toContainer(PASSENGER_RESPONSE_PAGE))
                .thenReturn(PASSENGER_CONTAINER_RESPONSE);

        PassengerContainerResponse actualDriverContainerResponse = passengerService.getAllPassengers(OFFSET, LIMIT);

        verify(passengerRepository).findByDeletedFalse(PageRequest.of(OFFSET, LIMIT));
        verify(passengerMapper).toDto(PASSENGER);
        verify(passengerContainerResponseMapper).toContainer(PASSENGER_RESPONSE_PAGE);
        assertEquals(PASSENGER_CONTAINER_RESPONSE, actualDriverContainerResponse);
    }

    @Test
    void getPassengerByPhone_whenPassengerExists_returnPassengerResponse() {
        when(passengerRepository.findByPhoneAndDeletedFalse(PASSENGER_PHONE))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse checkedPassengerResponse = passengerService.getPassengerByPhone(PASSENGER_PHONE);

        verify(passengerRepository).findByPhoneAndDeletedFalse(PASSENGER_PHONE);
        verify(passengerMapper).toDto(PASSENGER);
        assertEquals(PASSENGER_RESPONSE, checkedPassengerResponse);
    }

    @Test
    void getPassengerByPhone_whenPassengerNotExists_throwEntityNotFoundException() {
        when(passengerRepository.findByPhoneAndDeletedFalse(PASSENGER_PHONE))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> passengerService.getPassengerByPhone(PASSENGER_PHONE));
        verify(passengerRepository).findByPhoneAndDeletedFalse(PASSENGER_PHONE);
    }

    @Test
    void getPassengerByEmail_whenPassengerExists_returnPassengerResponse() {
        when(passengerRepository.findByEmailAndDeletedFalse(PASSENGER_EMAIL))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse checkedPassengerResponse = passengerService.getPassengerByEmail(PASSENGER_EMAIL);

        verify(passengerRepository).findByEmailAndDeletedFalse(PASSENGER_EMAIL);
        verify(passengerMapper).toDto(PASSENGER);
        assertEquals(PASSENGER_RESPONSE, checkedPassengerResponse);
    }

    @Test
    void getPassengerByEmail_whenPassengerNotExists_throwEntityNotFoundException() {
        when(passengerRepository.findByEmailAndDeletedFalse(PASSENGER_EMAIL))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> passengerService.getPassengerByEmail(PASSENGER_EMAIL));
        verify(passengerRepository).findByEmailAndDeletedFalse(PASSENGER_EMAIL);
    }

    @Test
    void softDeletePassenger_whenPassengerExists_thenSetDeletedToPassenger() {

        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerRepository.save(PASSENGER))
                .thenReturn(PASSENGER);

        passengerService.softDeletePassenger(PASSENGER_ID);

        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository).save(PASSENGER);
    }

    @Test
    void safeDeleteDriver_whenDriverNotExists_throwEntityNotFoundException() {

        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> passengerService.softDeletePassenger(PASSENGER_ID));
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository, never()).save(PASSENGER);
    }

    @Test
    void deletePassenger_whenPassengerExists_thenSuccessDelete() {

        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));

        passengerService.hardDeletePassenger(PASSENGER_ID);

        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository).delete(PASSENGER);
    }

    @Test
    void deletePassenger_whenPassengerNotExists_throwEntityNotFoundException() {

        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> passengerService.hardDeletePassenger(PASSENGER_ID));
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository, never()).delete(PASSENGER);
    }

    @Test
    void createPassenger_whenPassengerDeleted_thenReturnPassengerResponse() {
        when(passengerRepository.findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerRepository.save(PASSENGER))
                .thenReturn(PASSENGER);
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse actualPassengerResponse = passengerService.createPassenger(PASSENGER_REQUEST);

        assertEquals(PASSENGER_RESPONSE, actualPassengerResponse);
        verify(passengerRepository).findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE);
        verify(passengerRepository).save(PASSENGER);
        verify(passengerMapper).toDto(PASSENGER);
        verify(passengerMapper, never()).toEntity(PASSENGER_REQUEST);
    }

    @Test
    void createPassenger_whenPassengerNotDeletedAndEmailExists_throwResourceAlreadyExistsException() {
        when(passengerRepository.findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE))
                .thenReturn(Optional.empty());
        when(passengerRepository.existsByEmail(PASSENGER_EMAIL))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_EMAIL));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> passengerService.createPassenger(PASSENGER_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_EMAIL), ex.getMessage());

        verify(passengerRepository).findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE);
        verify(passengerRepository).existsByEmail(PASSENGER_EMAIL);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(passengerRepository, never()).save(PASSENGER);
    }

    @Test
    void createPassenger_whenPassengerNotDeletedAndEmailNotExistsAndPhoneExists_throwResourceAlreadyExistsException() {
        when(passengerRepository.findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE))
                .thenReturn(Optional.empty());
        when(passengerRepository.existsByEmail(PASSENGER_EMAIL))
                .thenReturn(FALSE);
        when(passengerRepository.existsByPhone(PASSENGER_PHONE))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_PHONE));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> passengerService.createPassenger(PASSENGER_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_PHONE), ex.getMessage());
        verify(passengerRepository).findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE);
        verify(passengerRepository).existsByEmail(PASSENGER_EMAIL);
        verify(passengerRepository).existsByPhone(PASSENGER_PHONE);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(passengerRepository, never()).save(PASSENGER);
    }

    @Test
    void createPassenger_whenPassengerNotDeletedAndEmailNotExistsAndPhoneNotExists_returnPassengerResponse() {
        when(passengerRepository.findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE))
                .thenReturn(Optional.empty());
        when(passengerRepository.existsByEmail(PASSENGER_EMAIL))
                .thenReturn(FALSE);
        when(passengerRepository.existsByPhone(PASSENGER_PHONE))
                .thenReturn(FALSE);
        when(passengerMapper.toEntity(PASSENGER_REQUEST))
                .thenReturn(PASSENGER);
        when(passengerRepository.save(PASSENGER))
                .thenReturn(PASSENGER);
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse actualPassengerResponse = passengerService.createPassenger(PASSENGER_REQUEST);

        assertEquals(PASSENGER_RESPONSE, actualPassengerResponse);
        verify(passengerRepository).findByNameAndEmailAndPhoneAndDeletedIsTrue(PASSENGER_REQUEST.name(), PASSENGER_EMAIL, PASSENGER_PHONE);
        verify(passengerRepository).existsByEmail(PASSENGER_EMAIL);
        verify(passengerRepository).existsByPhone(PASSENGER_PHONE);
        verify(passengerMapper).toEntity(PASSENGER_REQUEST);
        verify(passengerRepository).save(PASSENGER);
        verify(passengerMapper).toDto(PASSENGER);
    }

    @Test
    void updatePassenger_whenPassengerNotFound_throwEntityNotFoundException() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> passengerService.updatePassenger(PASSENGER_ID, PASSENGER_REQUEST));
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository, never()).save(PASSENGER);
    }

    @Test
    void updatePassenger_whenPassengerExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailExists_throwResourceAlreadyExistsException() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerRepository.existsByEmail(PASSENGER_UPDATED_EMAIL))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_UPDATED_EMAIL));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> passengerService.updatePassenger(PASSENGER_ID, PASSENGER_UPDATED_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_UPDATED_EMAIL), ex.getMessage());
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository).existsByEmail(PASSENGER_UPDATED_EMAIL);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(passengerRepository, never()).save(PASSENGER);
    }

    @Test
    void updatePassenger_whenPassengerExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailNotExistsAndCurrentPhoneIsNotUpdatedPhoneAndUpdatedPhoneExists_throwResourceAlreadyExistsException() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerRepository.existsByEmail(PASSENGER_UPDATED_EMAIL))
                .thenReturn(FALSE);
        when(passengerRepository.existsByPhone(PASSENGER_UPDATED_PHONE))
                .thenReturn(TRUE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_UPDATED_PHONE));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> passengerService.updatePassenger(PASSENGER_ID, PASSENGER_UPDATED_REQUEST));
        assertEquals(String.format(RESOURCE_ALREADY_EXISTS, PASSENGER_RESOURCE, PASSENGER_UPDATED_PHONE), ex.getMessage());
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository).existsByEmail(PASSENGER_UPDATED_EMAIL);
        verify(passengerRepository).existsByPhone(PASSENGER_UPDATED_PHONE);
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(passengerRepository, never()).save(PASSENGER);
    }

    @Test
    void updatePassenger_whenPassengerExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailNotExistsAndCurrentPhoneIsNotUpdatedPhoneAndUpdatedPhoneNotExists_returnPassengerResponse() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerRepository.existsByEmail(PASSENGER_UPDATED_EMAIL))
                .thenReturn(FALSE);
        when(passengerRepository.existsByPhone(PASSENGER_UPDATED_PHONE))
                .thenReturn(FALSE);
        when(passengerRepository.save(PASSENGER))
                .thenReturn(PASSENGER);
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse actualPassengerResponse = passengerService.updatePassenger(PASSENGER_ID, PASSENGER_UPDATED_REQUEST);

        assertEquals(PASSENGER_RESPONSE, actualPassengerResponse);
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository).existsByEmail(PASSENGER_UPDATED_EMAIL);
        verify(passengerRepository).existsByPhone(PASSENGER_UPDATED_PHONE);
        verify(passengerRepository).save(PASSENGER);
        verify(passengerMapper).toDto(PASSENGER);
    }

    @Test
    void updatePassenger_whenPassengerExistsAndCurrentEmailIsNotUpdatedEmailAndUpdatedEmailNotExistsAndCurrentPhoneIsUpdatedPhone_returnPassengerResponse() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerRepository.existsByEmail(PASSENGER_UPDATED_EMAIL))
                .thenReturn(FALSE);
        when(passengerRepository.save(PASSENGER))
                .thenReturn(PASSENGER);
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse actualPassengerResponse = passengerService.updatePassenger(PASSENGER_ID, PASSENGER_UPDATED_REQUEST);

        assertEquals(PASSENGER_RESPONSE, actualPassengerResponse);
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository).existsByEmail(PASSENGER_UPDATED_EMAIL);
        verify(passengerRepository).save(PASSENGER);
        verify(passengerMapper).toDto(PASSENGER);
    }

    @Test
    void updatePassenger_whenPassengerExistsAndCurrentEmailIsUpdatedEmailAndCurrentPhoneIsUpdatedPhone_returnPassengerResponse() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(PASSENGER));
        when(passengerRepository.save(PASSENGER))
                .thenReturn(PASSENGER);
        when(passengerMapper.toDto(PASSENGER))
                .thenReturn(PASSENGER_RESPONSE);

        PassengerResponse actualPassengerResponse = passengerService.updatePassenger(PASSENGER_ID, PASSENGER_REQUEST);

        assertEquals(PASSENGER_RESPONSE, actualPassengerResponse);
        verify(passengerRepository).findByIdAndDeletedFalse(PASSENGER_ID);
        verify(passengerRepository).save(PASSENGER);
        verify(passengerMapper).toDto(PASSENGER);
    }
}
