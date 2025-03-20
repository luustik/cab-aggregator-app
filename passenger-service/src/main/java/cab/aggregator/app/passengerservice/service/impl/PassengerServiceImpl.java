package cab.aggregator.app.passengerservice.service.impl;

import cab.aggregator.app.exception.common.AccessDeniedException;
import cab.aggregator.app.exception.common.EntityNotFoundException;
import cab.aggregator.app.exception.common.ResourceAlreadyExistsException;
import cab.aggregator.app.passengerservice.config.KeycloakProperties;
import cab.aggregator.app.passengerservice.dto.request.PassengerRequest;
import cab.aggregator.app.passengerservice.dto.request.PasswordRequest;
import cab.aggregator.app.passengerservice.dto.response.PassengerContainerResponse;
import cab.aggregator.app.passengerservice.dto.response.PassengerResponse;
import cab.aggregator.app.passengerservice.entity.Passenger;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static cab.aggregator.app.passengerservice.utility.CacheConstants.PASSENGER_CACHE;
import static cab.aggregator.app.passengerservice.utility.Constants.PASSENGER;
import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.EMAIL_CLAIM;
import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.ROLE_ADMIN;
import static cab.aggregator.app.passengerservice.utility.MessageKeys.ACCESS_DENIED_KEY;
import static cab.aggregator.app.passengerservice.utility.MessageKeys.ENTITY_NOT_FOUND_KEYCLOAK_KEY;
import static cab.aggregator.app.passengerservice.utility.MessageKeys.ENTITY_WITH_ID_NOT_FOUND_KEY;
import static cab.aggregator.app.passengerservice.utility.MessageKeys.ENTITY_WITH_RESOURCE_NOT_FOUND_KEY;
import static cab.aggregator.app.passengerservice.utility.MessageKeys.RESOURCE_ALREADY_EXIST_KEY;

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
    @Cacheable(value = PASSENGER_CACHE, key = "#id")
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
    @Cacheable(value = PASSENGER_CACHE, key = "#result.id()")
    public PassengerResponse getPassengerByPhone(String email) {
        return passengerMapper.toDto(findPassengerByPhone(email));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = PASSENGER_CACHE, key = "#result.id()")
    public PassengerResponse getPassengerByEmail(String email) {
        return passengerMapper.toDto(findPassengerByEmail(email));
    }

    @Override
    @Transactional
    @CacheEvict(value = PASSENGER_CACHE, key = "#id")
    public void softDeletePassenger(int id, JwtAuthenticationToken token) {
        Passenger passenger = findPassengerById(id);
        validateAccessOrThrow(passenger, token);
        passenger.setDeleted(true);
        passengerRepository.save(passenger);
    }

    @Override
    @Transactional
    @CacheEvict(value = PASSENGER_CACHE, key = "#id")
    public void hardDeletePassenger(int id) {
        Passenger passenger = findPassengerByIdForAdmin(id);
        deleteUserFromKeycloakByUsername(passenger.getEmail());
        passengerRepository.delete(passenger);
    }

    @Override
    @Transactional
    @CachePut(value = PASSENGER_CACHE, key = "#result.id()")
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
    @CachePut(value = PASSENGER_CACHE, key = "#id")
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
                    ENTITY_NOT_FOUND_KEYCLOAK_KEY,
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
            throw  new EntityNotFoundException(messageSource.getMessage(ENTITY_NOT_FOUND_KEYCLOAK_KEY,
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
                    ENTITY_NOT_FOUND_KEYCLOAK_KEY,
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

        String userEmail = token.getToken()
                .getClaims()
                .get(EMAIL_CLAIM)
                .toString();
        if (!passenger.getEmail().equals(userEmail)) {
            throw new AccessDeniedException(
                    messageSource.getMessage(ACCESS_DENIED_KEY,
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
            throw new ResourceAlreadyExistsException(
                    messageSource.getMessage(
                            RESOURCE_ALREADY_EXIST_KEY,
                            new Object[]{PASSENGER, passengerRequestDto.email()},
                            Locale.getDefault()));
        }
    }

    private void checkIfPhoneUnique(PassengerRequest passengerRequestDto) {
        if (passengerRepository.existsByPhone(passengerRequestDto.phone())) {
            throw new ResourceAlreadyExistsException(messageSource.getMessage(RESOURCE_ALREADY_EXIST_KEY, new Object[]{PASSENGER, passengerRequestDto.phone()}, Locale.getDefault()));
        }
    }

    private Passenger findPassengerById(int id) {
        return passengerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_KEY, new Object[]{PASSENGER, id}, Locale.getDefault()))
                );
    }

    private Passenger findPassengerByIdForAdmin(int id) {
        return passengerRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_ID_NOT_FOUND_KEY, new Object[]{PASSENGER, id}, Locale.getDefault()))
                );
    }

    private Passenger findPassengerByPhone(String phone) {
        return passengerRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_RESOURCE_NOT_FOUND_KEY, new Object[]{PASSENGER, phone}, Locale.getDefault()))
                );
    }

    private Passenger findPassengerByEmail(String email) {
        return passengerRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage(ENTITY_WITH_RESOURCE_NOT_FOUND_KEY, new Object[]{PASSENGER, email}, Locale.getDefault()))
                );
    }
}
