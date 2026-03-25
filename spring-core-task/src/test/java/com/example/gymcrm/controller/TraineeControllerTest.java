package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.TraineeProfileResponse;
import com.example.gymcrm.dto.response.TrainerSummaryDTO;
import com.example.gymcrm.dto.response.TrainingResponse;
import com.example.gymcrm.facade.GymFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private GymFacade facade;

    private TraineeController controller;

    private TraineeRegistrationRequest registrationRequest;
    private UpdateTraineeRequest updateRequest;
    private ActivateRequest activateRequest;
    private UpdateTraineeTrainersRequest updateTrainersRequest;
    private TraineeTrainingQueryRequest trainingQueryRequest;
    private TraineeProfileResponse profileResponse;
    private List<TrainerSummaryDTO> trainerSummaries;
    private List<TrainingResponse> trainingResponses;

    @BeforeEach
    void setUp() {
        controller = new TraineeController(facade);

        registrationRequest = new TraineeRegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        registrationRequest.setAddress("123 Main St");

        updateRequest = new UpdateTraineeRequest();
        updateRequest.setUsername("john.doe");
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Smith");
        updateRequest.setDateOfBirth(LocalDate.of(1991, 2, 2));
        updateRequest.setAddress("456 Oak St");

        activateRequest = new ActivateRequest();
        activateRequest.setUsername("john.doe");
        activateRequest.setIsActive(true);

        updateTrainersRequest = new UpdateTraineeTrainersRequest();
        updateTrainersRequest.setUsername("john.doe");
        updateTrainersRequest.setTrainerUsernames(List.of("trainer1", "trainer2"));

        trainingQueryRequest = new TraineeTrainingQueryRequest();
        trainingQueryRequest.setPeriodFrom(LocalDate.of(2024, 1, 1));
        trainingQueryRequest.setPeriodTo(LocalDate.of(2024, 12, 31));
        trainingQueryRequest.setTrainerName("Mike");
        trainingQueryRequest.setTrainingTypeId(1L);

        profileResponse = new TraineeProfileResponse(
                "john.doe", "John", "Doe",
                LocalDate.of(1990, 1, 1), "123 Main St",
                true, List.of()
        );

        trainerSummaries = List.of(
                new TrainerSummaryDTO("trainer1", "John", "Doe", "Cardio")
        );

        trainingResponses = List.of(
                new TrainingResponse("Morning Run", LocalDate.now(), "Cardio", 60, "trainer1")
        );
    }

    @Test
    void register_ShouldReturnAuthCredentials() {
        AuthCredentials expected = new AuthCredentials("john.doe", "password");
        when(facade.createTrainee(registrationRequest)).thenReturn(expected);

        AuthCredentials result = controller.register(registrationRequest);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(facade).createTrainee(registrationRequest);
    }

    @Test
    void get_ShouldReturnTraineeProfile() {
        when(facade.getTraineeByUsername(any(AuthCredentials.class), eq("john.doe")))
                .thenReturn(profileResponse);

        TraineeProfileResponse result = controller.get("john.doe", "john.doe", "password");

        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        assertEquals("John", result.getFirstName());
        verify(facade).getTraineeByUsername(any(AuthCredentials.class), eq("john.doe"));
    }

    @Test
    void update_ShouldReturnUpdatedProfile() {
        when(facade.updateTrainee(any(AuthCredentials.class), eq("john.doe"), eq(updateRequest)))
                .thenReturn(profileResponse);

        TraineeProfileResponse result = controller.update(updateRequest, "john.doe", "password");

        assertNotNull(result);
        verify(facade).updateTrainee(any(AuthCredentials.class), eq("john.doe"), eq(updateRequest));
    }

    @Test
    void delete_ShouldCallFacade() {
        controller.delete("john.doe", "john.doe", "password");

        verify(facade).deleteTrainee(any(AuthCredentials.class), eq("john.doe"));
    }

    @Test
    void activate_ShouldCallFacade() {
        controller.activate(activateRequest, "john.doe", "password");

        verify(facade).setTraineeActiveStatus(any(AuthCredentials.class), eq("john.doe"), eq(true));
    }

    @Test
    void unassigned_ShouldReturnList() {
        when(facade.getUnassignedTrainers(any(AuthCredentials.class), eq("john.doe")))
                .thenReturn(trainerSummaries);

        List<TrainerSummaryDTO> result = controller.unassigned("john.doe", "john.doe", "password");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(facade).getUnassignedTrainers(any(AuthCredentials.class), eq("john.doe"));
    }

    @Test
    void updateTrainers_ShouldReturnUpdatedList() {
        when(facade.updateTraineeTrainers(any(AuthCredentials.class), eq("john.doe"), eq(List.of("trainer1", "trainer2"))))
                .thenReturn(trainerSummaries);

        List<TrainerSummaryDTO> result = controller.updateTrainers(updateTrainersRequest, "john.doe", "password");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(facade).updateTraineeTrainers(any(AuthCredentials.class), eq("john.doe"), eq(List.of("trainer1", "trainer2")));
    }

    @Test
    void getTrainings_ShouldReturnList() {
        when(facade.getTraineeTrainings(
                any(AuthCredentials.class), eq("john.doe"),
                eq(trainingQueryRequest.getPeriodFrom()),
                eq(trainingQueryRequest.getPeriodTo()),
                eq(trainingQueryRequest.getTrainerName()),
                eq(trainingQueryRequest.getTrainingTypeId())))
                .thenReturn(trainingResponses);

        List<TrainingResponse> result = controller.getTrainings(
                "john.doe", trainingQueryRequest, "john.doe", "password");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(facade).getTraineeTrainings(
                any(AuthCredentials.class), eq("john.doe"),
                eq(trainingQueryRequest.getPeriodFrom()),
                eq(trainingQueryRequest.getPeriodTo()),
                eq(trainingQueryRequest.getTrainerName()),
                eq(trainingQueryRequest.getTrainingTypeId()));
    }

    @Test
    void getTrainings_ShouldHandleNullQueryParameters() {
        TraineeTrainingQueryRequest emptyQuery = new TraineeTrainingQueryRequest();

        when(facade.getTraineeTrainings(
                any(AuthCredentials.class), eq("john.doe"),
                isNull(), isNull(), isNull(), isNull()))
                .thenReturn(trainingResponses);

        List<TrainingResponse> result = controller.getTrainings(
                "john.doe", emptyQuery, "john.doe", "password");

        assertNotNull(result);
        verify(facade).getTraineeTrainings(
                any(AuthCredentials.class), eq("john.doe"),
                isNull(), isNull(), isNull(), isNull());
    }
}