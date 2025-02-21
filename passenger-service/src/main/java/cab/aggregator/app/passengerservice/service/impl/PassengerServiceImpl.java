package cab.aggregator.app.passengerservice.service.impl;

import cab.aggregator.app.passengerservice.config.KeycloakProperties;
import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.dto.request.PasswordRequest;
import cab.aggregator.app.passengerservice.dto.response.PassengerContainerResponse;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import cab.aggregator.app.passengerservice.entity.Passenger;
import cab.aggregator.app.passengerservice.exception.AccessDeniedException;
import cab.aggregator.app.passengerservice.exception.EntityNotFoundException;
import cab.aggregator.app.passengerservice.exception.ResourceAlreadyExistsException;
import cab.aggregator.app.passengerservice.mapper.PassengerContainerMapper;
import cab.aggregator.app.passengerservice.mapper.PassengerMapper;
import cab.aggregator.app.passengerservice.repository.PassengerRepository;
import cab.aggregator.app.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static cab.aggregator.app.passengerservice.utility.Constants.ACCESS_DENIED_MESSAGE;
import static cab.aggregator.app.passengerservice.utility.Constants.EMAIL_CLAIM;
import static cab.aggregator.app.passengerservice.utility.Constants.ENTITY_NOT_FOUND_KEYCLOAK_MESSAGE;
import static cab.aggregator.app.passengerservice.utility.Constants.PASSENGER;
import static cab.aggregator.app.passengerservice.utility.Constants.RESOURCE_ALREADY_EXIST_MESSAGE;
import static cab.aggregator.app.passengerservice.utility.Constants.ENTITY_WITH_ID_NOT_FOUND_MESSAGE;
import static cab.aggregator.app.passengerservice.utility.Constants.ENTITY_WITH_RESOURCE_NOT_FOUND_MESSAGE;
import static cab.aggregator.app.passengerservice.utility.Constants.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final PassengerContainerMapper passengerContainerMapper;
    private final MessageSource messageSource;
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse getPassengerById(int id) {
        return passengerMapper.toDto(findPassengerById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerContainerResponse getAllPassengersAdmin(int offset, int limit) {
        return passengerContainerMapper.toContainer(passengerRepository
                .findAll(PageRequest.of(offset, limit))
                .map(passengerMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerContainerResponse getAllPassengers(int offset, int limit) {
        return passengerContainerMapper.toContainer(passengerRepository
                .findByDeletedFalse(PageRequest.of(offset, limit))
                .map(passengerMapper::toDto));
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse getPassengerByPhone(String email) {
        return passengerMapper.toDto(findPassengerByPhone(email));
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse getPassengerByEmail(String email) {
        return passengerMapper.toDto(findPassengerByEmail(email));
    }

    @Override
    @Transactional
    public void softDeletePassenger(int id, JwtAuthenticationToken token) {
        Passenger passenger = findPassengerById(id);
        validateAccessOrThrow(passenger, token);
        passenger.setDeleted(true);
        passengerRepository.save(passenger);
    }

    @Override
    @Transactional
    public void hardDeletePassenger(int id) {
        Passenger passenger = findPassengerByIdForAdmin(id);
        deleteUserFromKeycloakByUsername(passenger.getEmail());
        passengerRepository.delete(passenger);
    }

    @Override
    @Transactional
    public PassengerResponse createPassenger(PassengerRequest passengerRequestDto) {
        Passenger passenger = checkIfPassengerDelete(passengerRequestDto);
        if (passenger != null) {
            passenger.setDeleted(false);
            passengerRepository.save(passenger);
            return passengerMapper.toDto(passenger);
        }
        checkIfPassengerUnique(passengerRequestDto);
        passenger = passengerMapper.toEntity(passengerRequestDto);
        passengerRepository.save(passenger);
        return passengerMapper.toDto(passenger);
    }

    @Override
    @Transactional
    public PassengerResponse updatePassenger(int id, PassengerRequest passengerRequestDto, JwtAuthenticationToken token) {
        Passenger passenger = findPassengerById(id);
        String email = passenger.getEmail();
        if (!passengerRequestDto.email().equals(passenger.getEmail())) {
            checkIfEmailUnique(passengerRequestDto);
        }
        if (!passengerRequestDto.phone().equals(passenger.getPhone())) {
            checkIfPhoneUnique(passengerRequestDto);
        }
        validateAccessOrThrow(passenger, token);
        passengerMapper.updatePassengerFromDto(passengerRequestDto, passenger);
        passenger.setDeleted(false);
        updateUserInKeycloak(email,passenger);
        passengerRepository.save(passenger);
        return passengerMapper.toDto(passenger);
    }

    @Override
    @Transactional
    public void updatePassword(int id, PasswordRequest request, JwtAuthenticationToken token) {
        Passenger passenger = findPassengerById(id);
        validateAccessOrThrow(passenger, token);
        updatePasswordUserInKeycloak(passenger.getEmail(), request.password());
    }

    private void updatePasswordUserInKeycloak(String email, String password) {
        UsersResource usersResource = keycloak.realm(keycloakProperties.getRealm()).users();
        List<UserRepresentation> users = usersResource.search(email, true);

        if (users == null || users.isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage(
                    ENTITY_NOT_FOUND_KEYCLOAK_MESSAGE,
                    new Object[]{email}, Locale.getDefault()));
        }

        String userId = users.get(0).getId();
        UserRepresentation userRepresentation = users.get(0);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        userRepresentation.setCredentials(List.of(credential));

        usersResource.get(userId).update(userRepresentation);
    }

    private void deleteUserFromKeycloakByUsername(String username) {
        UsersResource usersResource = keycloak.realm(keycloakProperties.getRealm()).users();
        List<UserRepresentation> users = usersResource.search(username, true);

        if (users == null || users.isEmpty()) {
            throw  new EntityNotFoundException(messageSource.getMessage(ENTITY_NOT_FOUND_KEYCLOAK_MESSAGE,
                    new Object[]{}, Locale.getDefault()));
        }

        String userId = users.get(0).getId();

        RealmResource realm = keycloak.realm(keycloakProperties.getRealm());
        realm.users().delete(userId);
    }

    private void updateUserInKeycloak(String email, Passenger passenger) {
        UsersResource usersResource = keycloak.realm(keycloakProperties.getRealm()).users();
        List<UserRepresentation> users = usersResource.search(email, true);

        if (users == null || users.isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage(
                    ENTITY_NOT_FOUND_KEYCLOAK_MESSAGE,
                    new Object[]{email}, Locale.getDefault()));
        }

        String userId = users.get(0).getId();
        UserRepresentation userRepresentation = users.get(0);

        userRepresentation.setFirstName(passenger.getName());
        userRepresentation.setEmail(passenger.getEmail());
        userRepresentation.setEnabled(!passenger.isDeleted());

        usersResource.get(userId).update(userRepresentation);
    }

    private void validateAccessOrThrow(Passenger passenger, JwtAuthenticationToken token) {
        if (token.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN))) {
            return;
        }

        String userEmail = token.getToken().getClaims().get(EMAIL_CLAIM).toString();
        if (!passenger.getEmail().equals(userEmail)) {
            throw new AccessDeniedException(
                    messageSource.getMessage(ACCESS_DENIED_MESSAGE,
                            new Object[]{}, LocaleContextHolder.getLocale())
            );
        }
    }

    private Passenger checkIfPassengerDelete(PassengerRequest passengerRequestDto) {
        return passengerRepository
                .findByNameAndEmailAndPhoneAndDeletedIsTrue(passengerRequestDto.name(),
                        passengerRequestDto.email(),
                        passengerRequestDto.phone())
                .orElse(null);
    }

    private void checkIfPassengerUnique(PassengerRequest passengerRequestDto) {
        checkIfEmailUnique(passengerRequestDto);
        checkIfPhoneUnique(passengerRequestDto);
    }

    private void checkIfEmailUnique(PassengerRequest passengerRequestDto) {
        if (passengerRepository.existsByEmail(passengerRequestDto.email())) {
            throw new ResourceAlreadyExistsException(messageSource.getMessage(RESOURCE_ALREADY_EXIST_MESSAGE, new Object[]{PASSENGER, passengerRequestDto.email()}, Locale.getDefault()));
        }
    }

    private void checkIfPhoneUnique(PassengerRequest passengerRequestDto) {
        if (passengerRepository.existsByPhone(passengerRequestDto.phone())) {
            throw new ResourceAlreadyExistsException(messageSource.getMessage(RESOURCE_ALREADY_EXIST_MESSAGE, new Object[]{PASSENGER, passengerRequestDto.phone()}, Locale.getDefault()));
        }
    }

    private Passenger findPassengerById(int id) {
        return passengerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_MESSAGE, new Object[]{PASSENGER, id}, Locale.getDefault()))
                );
    }

    private Passenger findPassengerByIdForAdmin(int id) {
        return passengerRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_MESSAGE, new Object[]{PASSENGER, id}, Locale.getDefault()))
                );
    }

    private Passenger findPassengerByPhone(String phone) {
        return passengerRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_RESOURCE_NOT_FOUND_MESSAGE, new Object[]{PASSENGER, phone}, Locale.getDefault()))
                );
    }

    private Passenger findPassengerByEmail(String email) {
        return passengerRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_RESOURCE_NOT_FOUND_MESSAGE, new Object[]{PASSENGER, email}, Locale.getDefault()))
                );
    }
}
