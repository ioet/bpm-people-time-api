package com.ioet.bpm.peopletime.boundaries;

import com.ioet.bpm.peopletime.domain.TimeEvent;
import com.ioet.bpm.peopletime.domain.TimeEvent;
import com.ioet.bpm.peopletime.repositories.TimeEventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TimeEventControllerTest {

    @Mock
    private TimeEventRepository timeEventRepository;

    @InjectMocks
    private TimeEventController timeEventController;

    @Test
    public void eventsAreListedUsingTheRepository() {

        ResponseEntity<Iterable> events = timeEventController.getAllTimeEvents();

        assertEquals(HttpStatus.OK, events.getStatusCode());
        verify(timeEventRepository, times(1)).findAll();
    }

    @Test
    public void whenAEventIsCreatedTheNewEventIsReturnedCorrectly() {
        TimeEvent eventCreated = mock(TimeEvent.class);

        TimeEvent eventToCreate = new TimeEvent();
        eventToCreate.setPersonId("somePersonId");
        eventToCreate.setActivity("I did some things.");

        when(timeEventRepository.save(eventToCreate)).thenReturn(eventCreated);

        ResponseEntity<TimeEvent> eventCreatedResponse;

        eventCreatedResponse = timeEventController.createTimeEvent(eventToCreate);

        assertEquals(eventCreated, eventCreatedResponse.getBody());
        assertEquals(HttpStatus.CREATED, eventCreatedResponse.getStatusCode());
        verify(timeEventRepository, times(1)).save(eventToCreate);

    }

    @Test
    public void whenATemplateDoesNotExistA404IsExpected() {
        ResponseEntity<TimeEvent> notExistingTemplateResponse = timeEventController.getTimeEvent("not_existing_id");

        assertNull(notExistingTemplateResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, notExistingTemplateResponse.getStatusCode());
    }

    @Test
    public void aTemplateIsFoundUsingTheRepository() {
        String eventId = "eventId";

        timeEventController.getTimeEvent(eventId);

        verify(timeEventRepository, times(1)).findById(eventId);
    }

    @Test
    public void theBodyContainsTheTemplateFromTheRepository() {
        String eventIdToFind = "id";
        TimeEvent eventFound = mock(TimeEvent.class);
        when(timeEventRepository.findById(eventIdToFind)).thenReturn(Optional.of(eventFound));

        ResponseEntity<TimeEvent> existingTemplateResponse = timeEventController.getTimeEvent(eventIdToFind);

        assertEquals(eventFound, existingTemplateResponse.getBody());
        assertEquals(HttpStatus.OK, existingTemplateResponse.getStatusCode());
    }

    @Test
    public void ifTheTemplateToDeleteDoesNotExistA404IsReturned() {
        String id = "id";
        when(timeEventRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = timeEventController.deleteTimeEvent(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenDeletingATemplateTheResponseIsEmpty() {
        String eventIdToDelete = "id";
        TimeEvent eventToDelete = mock(TimeEvent.class);
        when(timeEventRepository.findById(eventIdToDelete)).thenReturn(Optional.of(eventToDelete));

        ResponseEntity<?> deletedTemplateResponse = timeEventController.deleteTimeEvent(eventIdToDelete);

        assertNull(deletedTemplateResponse.getBody());
        assertEquals(HttpStatus.NO_CONTENT, deletedTemplateResponse.getStatusCode());
        verify(timeEventRepository, times(1)).delete(eventToDelete);
    }

    @Test
    public void ifTheTemplateToUpdateDoesNotExistA404IsReturned() {
        String id = "id";
        when(timeEventRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = timeEventController.updateTimeEvent(id, mock(TimeEvent.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenATemplateIsUpdatedTheUpdatedTemplateIsReturned() {
        TimeEvent eventUpdated = mock(TimeEvent.class);
        Optional<TimeEvent> eventFound = Optional.of(mock(TimeEvent.class));

        String idTemplateToUpdate = "id";
        TimeEvent eventToUpdate = mock(TimeEvent.class);

        when(timeEventRepository.findById(idTemplateToUpdate)).thenReturn(eventFound);
        when(timeEventRepository.save(eventToUpdate)).thenReturn(eventUpdated);

        ResponseEntity<TimeEvent> updatedTemplateResponse = timeEventController.updateTimeEvent(idTemplateToUpdate, eventToUpdate);

        assertEquals(eventUpdated, updatedTemplateResponse.getBody());
        assertEquals(HttpStatus.OK, updatedTemplateResponse.getStatusCode());
        verify(timeEventRepository, times(1)).save(eventToUpdate);
    }
}
