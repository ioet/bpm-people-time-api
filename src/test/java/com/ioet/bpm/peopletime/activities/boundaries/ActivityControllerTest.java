package com.ioet.bpm.peopletime.activities.boundaries;

import com.ioet.bpm.peopletime.activities.domain.Activity;
import com.ioet.bpm.peopletime.activities.repositories.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityControllerTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityController activityController;

    @Test
    void whenGettingAllActivitiesTheyAreReturned() {
        Iterable<Activity> allActivities = mock(Iterable.class);
        when(activityRepository.findAll()).thenReturn(allActivities);

        ResponseEntity<Iterable> activitiesResponse = activityController.getAllActivities();

        assertEquals(HttpStatus.OK, activitiesResponse.getStatusCode());
        assertEquals(allActivities, activitiesResponse.getBody());
        verify(activityRepository).findAll();
    }

    @Test
    void whenGettingOneActivityOnlyThisIsReturned() {
        String activityId = "id";
        Activity oneActivity = mock(Activity.class);
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(oneActivity));

        ResponseEntity<Activity> activityResponse = activityController.getActivityById(activityId);

        assertEquals(HttpStatus.OK, activityResponse.getStatusCode());
        assertEquals(oneActivity, activityResponse.getBody());
        verify(activityRepository).findById(activityId);
    }

    @Test
    void whenActivityForIdIsNotFoundReturn404() {
        String activityId = "id";
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        ResponseEntity<Activity> activityResponse = activityController.getActivityById(activityId);

        assertEquals(HttpStatus.NOT_FOUND, activityResponse.getStatusCode());
        assertNull(activityResponse.getBody());
        verify(activityRepository).findById(activityId);
    }

    @Test
    void whenCreatingNewActivityTheNewActivityIsReturned() {
        Activity activityToCreate = mock(Activity.class);
        Activity createdActivity = mock(Activity.class);
        when(activityRepository.save(activityToCreate)).thenReturn(createdActivity);

        ResponseEntity<Activity> organizationResponse = activityController.createActivity(activityToCreate);

        assertEquals(HttpStatus.CREATED, organizationResponse.getStatusCode());
        assertEquals(createdActivity, organizationResponse.getBody());
        verify(activityRepository).save(activityToCreate);
    }

    @Test
    void whenDeletingAnActivityA204IsReturned() {
        String id = "id";
        Activity oneActivity = mock(Activity.class);
        when(activityRepository.findById(id)).thenReturn(Optional.of(oneActivity));

        ResponseEntity deleteResponse = activityController.deleteActivity(id);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        verify(activityRepository).delete(oneActivity);
    }

    @Test
    void whenActivityToDeleteIsNotFoundReturned404() {
        String activityId = "id";
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        ResponseEntity deleteResponse = activityController.deleteActivity(activityId);

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
        verify(activityRepository, never()).deleteById(activityId);
    }

    @Test
    void whenActivityIsUpdatedThenReturnTheActivityAnd200() {
        Activity activityToUpdate = mock(Activity.class);
        Activity updatedActivity = mock(Activity.class);
        Optional<Activity> activityFound = Optional.of(mock(Activity.class));
        String activityId = "id";
        when(activityRepository.findById(activityId)).thenReturn(activityFound);
        when(activityRepository.save(activityToUpdate)).thenReturn(updatedActivity);

        ResponseEntity<Activity> updatedActivityResponse = activityController.updateActivity(activityId, activityToUpdate);

        assertEquals(HttpStatus.OK, updatedActivityResponse.getStatusCode());
        assertEquals(updatedActivity, updatedActivityResponse.getBody());
        verify(activityRepository).save(activityToUpdate);
    }

    @Test
    void whenANotFoundOrganizationIsUpdatedThenReturn400() {
        String activityId = "id";
        Activity activityToUpdate = mock(Activity.class);
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        ResponseEntity<?> updateActivityResponse = activityController.updateActivity(activityId, activityToUpdate);

        assertEquals(HttpStatus.NOT_FOUND, updateActivityResponse.getStatusCode());
        verify(activityRepository).findById(activityId);
    }
}
