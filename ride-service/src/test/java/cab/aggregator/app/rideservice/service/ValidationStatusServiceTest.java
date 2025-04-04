package cab.aggregator.app.rideservice.service;

import cab.aggregator.app.rideservice.entity.enums.Status;
import cab.aggregator.app.rideservice.exception.ImpossibleStatusException;
import cab.aggregator.app.rideservice.service.impl.ValidationStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static cab.aggregator.app.rideservice.entity.enums.Status.ACCEPTED;
import static cab.aggregator.app.rideservice.entity.enums.Status.CANCELLED;
import static cab.aggregator.app.rideservice.entity.enums.Status.COMPLETED;
import static cab.aggregator.app.rideservice.entity.enums.Status.CREATED;
import static cab.aggregator.app.rideservice.entity.enums.Status.WAY_TO_DESTINATION;
import static cab.aggregator.app.rideservice.entity.enums.Status.WAY_TO_PASSENGER;
import static cab.aggregator.app.rideservice.utils.RideConstants.IMPOSSIBLE_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationStatusServiceTest {

    @Mock
    MessageSource messageSource;

    @InjectMocks
    ValidationStatusService validationStatusService;

    //CREATED_STATUS
    @Test
    void validateStatus_whenCurrentStatusCreatedAndSubstituteStatusCreated_throwImpossibleStatusException() {
        assertInvalidStatus(CREATED, CREATED);
    }

    @Test
    void validateStatus_whenCurrentStatusCreatedAndSubstituteStatusAccepted_returnStatusAccepted() {
        assertValidStatus(CREATED, ACCEPTED);
    }

    @Test
    void validateStatus_whenCurrentStatusCreatedAndSubstituteStatusWayToPassenger_throwImpossibleStatusException() {
        assertInvalidStatus(CREATED, WAY_TO_PASSENGER);
    }

    @Test
    void validateStatus_whenCurrentStatusCreatedAndSubstituteStatusWayToDestination_throwImpossibleStatusException() {
        assertInvalidStatus(CREATED, WAY_TO_DESTINATION);
    }

    @Test
    void validateStatus_whenCurrentStatusCreatedAndSubstituteStatusCompleted_throwImpossibleStatusException() {
        assertInvalidStatus(CREATED, COMPLETED);
    }

    @Test
    void validateStatus_whenCurrentStatusCreatedAndSubstituteStatusCancelled_returnStatusCancelled() {
        assertValidStatus(CREATED, CANCELLED);
    }

    //ACCEPTED_STATUS
    @Test
    void validateStatus_whenCurrentStatusAcceptedAndSubstituteStatusCreated_throwImpossibleStatusException() {
        assertInvalidStatus(ACCEPTED, CREATED);
    }

    @Test
    void validateStatus_whenCurrentStatusAcceptedAndSubstituteStatusAccepted_throwImpossibleStatusException() {
        assertInvalidStatus(ACCEPTED, ACCEPTED);
    }

    @Test
    void validateStatus_whenCurrentStatusAcceptedAndSubstituteStatusWayToPassenger_returnStatusWayToPassenger() {
        assertValidStatus(ACCEPTED, WAY_TO_PASSENGER);
    }

    @Test
    void validateStatus_whenCurrentStatusAcceptedAndSubstituteStatusWayToDestination_throwImpossibleStatusException() {
        assertInvalidStatus(ACCEPTED, WAY_TO_DESTINATION);
    }

    @Test
    void validateStatus_whenCurrentStatusAcceptedAndSubstituteStatusCompleted_throwImpossibleStatusException() {
        assertInvalidStatus(ACCEPTED, COMPLETED);
    }

    @Test
    void validateStatus_whenCurrentStatusAcceptedAndSubstituteStatusCancelled_returnStatusCancelled() {
        assertValidStatus(ACCEPTED, CANCELLED);
    }

    //WAY_TO_PASSENGER_STATUS
    @Test
    void validateStatus_whenCurrentStatusWayToPassengerAndSubstituteStatusCreated_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_PASSENGER, CREATED);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToPassengerAndSubstituteStatusAccepted_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_PASSENGER, ACCEPTED);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToPassengerAndSubstituteStatusWayToPassenger_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_PASSENGER, WAY_TO_PASSENGER);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToPassengerAndSubstituteStatusWayToDestination_returnStatusWayToDestination() {
        assertValidStatus(WAY_TO_PASSENGER, WAY_TO_DESTINATION);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToPassengerAndSubstituteStatusCompleted_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_PASSENGER, COMPLETED);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToPassengerAndSubstituteStatusCancelled_returnStatusCancelled() {
        assertValidStatus(WAY_TO_PASSENGER, CANCELLED);
    }

    //WAY_TO_DESTINATION_STATUS
    @Test
    void validateStatus_whenCurrentStatusWayToDestinationAndSubstituteStatusCreated_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_DESTINATION, CREATED);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToDestinationAndSubstituteStatusAccepted_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_DESTINATION, ACCEPTED);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToDestinationAndSubstituteStatusWayToPassenger_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_DESTINATION, WAY_TO_PASSENGER);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToDestinationAndSubstituteStatusWayToDestination_throwImpossibleStatusException() {
        assertInvalidStatus(WAY_TO_DESTINATION, WAY_TO_DESTINATION);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToDestinationAndSubstituteStatusCompleted_returnStatusCompleted() {
        assertValidStatus(WAY_TO_DESTINATION, COMPLETED);
    }

    @Test
    void validateStatus_whenCurrentStatusWayToDestinationAndSubstituteStatusCancelled_returnStatusCancelled() {
        assertValidStatus(WAY_TO_DESTINATION, CANCELLED);
    }

    //COMPLETED_STATUS
    @Test
    void validateStatus_whenCurrentStatusCompletedAndSubstituteStatusCreated_throwImpossibleStatusException() {
        assertInvalidStatus(COMPLETED, CREATED);
    }

    @Test
    void validateStatus_whenCurrentStatusCompletedAndSubstituteStatusAccepted_throwImpossibleStatusException() {
        assertInvalidStatus(COMPLETED, ACCEPTED);
    }

    @Test
    void validateStatus_whenCurrentStatusCompletedAndSubstituteStatusWayToPassenger_throwImpossibleStatusException() {
        assertInvalidStatus(COMPLETED, WAY_TO_PASSENGER);
    }

    @Test
    void validateStatus_whenCurrentStatusCompletedAndSubstituteStatusWayToDestination_throwImpossibleStatusException() {
        assertInvalidStatus(COMPLETED, WAY_TO_DESTINATION);
    }

    @Test
    void validateStatus_whenCurrentStatusCompletedAndSubstituteStatusCompleted_throwImpossibleStatusException() {
        assertInvalidStatus(COMPLETED, COMPLETED);
    }

    @Test
    void validateStatus_whenCurrentStatusCompletedAndSubstituteStatusCancelled_throwImpossibleStatusException() {
        assertInvalidStatus(COMPLETED, CANCELLED);
    }

    //CANCELLED_STATUS
    @Test
    void validateStatus_whenCurrentStatusCancelledAndSubstituteStatusCreated_throwImpossibleStatusException() {
        assertInvalidStatus(CANCELLED, CREATED);
    }

    @Test
    void validateStatus_whenCurrentStatusCancelledAndSubstituteStatusAccepted_throwImpossibleStatusException() {
        assertInvalidStatus(CANCELLED, ACCEPTED);
    }

    @Test
    void validateStatus_whenCurrentStatusCancelledAndSubstituteStatusWayToPassenger_throwImpossibleStatusException() {
        assertInvalidStatus(CANCELLED, WAY_TO_PASSENGER);
    }

    @Test
    void validateStatus_whenCurrentStatusCancelledAndSubstituteStatusWayToDestination_throwImpossibleStatusException() {
        assertInvalidStatus(CANCELLED, WAY_TO_DESTINATION);
    }

    @Test
    void validateStatus_whenCurrentStatusCancelledAndSubstituteStatusCompleted_throwImpossibleStatusException() {
        assertInvalidStatus(CANCELLED, COMPLETED);
    }

    @Test
    void validateStatus_whenCurrentStatusCancelledAndSubstituteStatusCancelled_throwImpossibleStatusException() {
        assertInvalidStatus(CANCELLED, CANCELLED);
    }

    private void assertValidStatus(Status currentStatus, Status substituteStatus) {
        assertEquals(substituteStatus, validationStatusService.validateStatus(currentStatus, substituteStatus));
    }

    private void assertInvalidStatus(Status currentStatus, Status substituteStatus) {
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(IMPOSSIBLE_STATUS);

        ImpossibleStatusException ex = assertThrows(ImpossibleStatusException.class,
                () -> validationStatusService.validateStatus(currentStatus, substituteStatus));
        assertEquals(IMPOSSIBLE_STATUS, ex.getMessage());
    }
}


