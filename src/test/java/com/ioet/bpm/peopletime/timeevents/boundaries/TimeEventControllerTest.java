package com.ioet.bpm.peopletime.timeevents.boundaries;

import com.ioet.bpm.peopletime.skills.domain.Skill;
import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import com.ioet.bpm.peopletime.timeevents.services.LastActiveEventService;
import com.ioet.bpm.peopletime.timeevents.services.TimeEventService;
import com.ioet.bpm.peopletime.timetemplates.domain.TimeTemplate;
import com.ioet.bpm.peopletime.timetemplates.repositories.TimeTemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeEventControllerTest {

    @Mock
    private TimeEventRepository timeEventRepository;

    @Mock
    private TimeTemplateRepository timeTemplateRepository;

    @InjectMocks
    private TimeEventController timeEventController;

    @Mock
    TimeEventService timeEventService;

    @Mock
    LastActiveEventService lastActiveEventService;


    private Optional<TimeTemplate> mockFindByIdInTimeTemplateRepository(String existingTemplateId, String userId) {
        Optional<TimeTemplate> timeTemplateOptional = buildTimeTemplate(existingTemplateId, userId);
        when(timeTemplateRepository.findById(existingTemplateId)).thenReturn(timeTemplateOptional);
        return timeTemplateOptional;
    }

    private Optional<TimeTemplate> buildTimeTemplate(String existingTemplateId, String userId) {
        return Optional.of(TimeTemplate.builder().name("foo").activity("development")
                .organizationId("ioet-id").organizationName("ioet-name")
                .projectId("bpm-id").projectName("bpm-name")
                .personId(userId).skills(createSkillsList())
                .id(existingTemplateId).build());
    }

    private Optional<TimeEvent> mockFindActiveTimeEventInTemplateRepository(String userId) {
        Optional<TimeEvent> timeEventOptional = buildTimeEvent(userId);
        when(timeEventRepository.findByStopTimeIsNullAndPersonId(userId)).thenReturn(timeEventOptional);
        return timeEventOptional;
    }

    private Optional<TimeEvent> buildTimeEvent(String userId) {
        return Optional.of(TimeEvent.builder().activity("development").note("did some work")
                .organizationId("ioet-id").organizationName("ioet-name")
                .id("someId").personId(userId).skills(createSkillsList())
                .projectId("bpm-id").projectName("bpm-name")
                .startTime(new Date()).templateId("existingId").build());
    }

    private ArrayList<Skill> createSkillsList() {
        ArrayList<Skill> skills = new ArrayList<>();
        skills.add(new Skill("test-id", "test-skill"));
        return skills;
    }

    private void checkIfAllTheFieldsAreTheSame(TimeTemplate template, TimeEvent event) {
        assertAll("check that time-template and time-event contain the same information",
                () -> assertEquals(template.getPersonId(), event.getPersonId()),
                () -> assertEquals(template.getOrganizationId(), event.getOrganizationId()),
                () -> assertEquals(template.getOrganizationName(), event.getOrganizationName()),
                () -> assertEquals(template.getProjectId(), event.getProjectId()),
                () -> assertEquals(template.getProjectName(), event.getProjectName()),
                () -> assertEquals(template.getActivity(), event.getActivity()),
                () -> assertEquals(template.getId(), event.getTemplateId()),
                () -> assertEquals(template.getSkills(), event.getSkills())
        );
    }

    @Test
    void ifTheTemplateIdDoesNotExistA404IsReturned() {

        String nonExistingTemplateId = "foo";
        String userId = "bar";
        when(timeTemplateRepository.findById(nonExistingTemplateId)).thenReturn(Optional.empty());

        ResponseEntity responseEntity = timeEventController.startTimeEvent(nonExistingTemplateId, userId);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, responseStatus);
    }

    @Test
    void theGivenTemplateIsUsedToCreateANewTimeEvent() {
        String existingTemplateId = "existingId";
        String userId = "bar";
        Optional<TimeTemplate> timeTemplateOptional = mockFindByIdInTimeTemplateRepository(existingTemplateId, userId);

        timeEventController.startTimeEvent(existingTemplateId, userId);

        ArgumentCaptor<TimeTemplate> savedEventCaptor = ArgumentCaptor.forClass(TimeTemplate.class);
        ArgumentCaptor<String> savedUsernameCaptor = ArgumentCaptor.forClass(String.class);
        verify(timeEventService, times(1)).createNewTimeEvent(savedEventCaptor.capture(), savedUsernameCaptor.capture());

        TimeTemplate givenTemplate = savedEventCaptor.getValue();
        String givenUsername = savedUsernameCaptor.getValue();
        assertEquals(timeTemplateOptional.get(), givenTemplate);
        assertEquals(userId, givenUsername);
    }

    @Test
    void onceATimeEventIsStartedA200WithTheEventInfoIsReturned() {
        String existingTemplateId = "existingId";
        String userId = "bar";
        Optional<TimeTemplate> timeTemplateOptional = mockFindByIdInTimeTemplateRepository(existingTemplateId, userId);

        TimeEvent savedTimeEvent = buildTimeEvent(userId).get();
        when(timeEventService.createNewTimeEvent(any(TimeTemplate.class), anyString())).thenReturn(savedTimeEvent);

        ResponseEntity response = timeEventController.startTimeEvent(existingTemplateId, userId);

        TimeEvent timeEventCreated = (TimeEvent) response.getBody();
        checkIfAllTheFieldsAreTheSame(timeTemplateOptional.get(), timeEventCreated);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenAllEventsForOnePersonAreRequestedA200WithOnlyTheseEventsAreReturned() {
        String userId = "someUserId";
        Iterable<TimeEvent> timeEventsInRepository = mock(Iterable.class);

        doReturn(timeEventsInRepository).when(lastActiveEventService).getLastActiveTimeEvents(null, userId, 10);
        ResponseEntity<Iterable> response = timeEventController.findTimeEvents(userId, null, 10);

        Iterable optionalIterable = response.getBody();
        assertEquals(timeEventsInRepository, optionalIterable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lastActiveEventService).getLastActiveTimeEvents(null, userId, 10);
    }

    @Test
    void ifAUserHasNoActiveTimeEventA404IsReturned() {
        String userId = "bar";
        when(timeEventRepository.findByStopTimeIsNullAndPersonId(userId)).thenReturn(Optional.empty());

        ResponseEntity responseEntity = timeEventController.stopTimeEvent(userId);

        HttpStatus responseStatus = responseEntity.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseStatus);
    }

    @Test
    void onceATimeEventIsStoppedA200WithTheTimeEventIsReturned() {
        String userId = "userId";
        Optional<TimeEvent> timeEventOptional = mockFindActiveTimeEventInTemplateRepository(userId);
        when(timeEventService.saveStopTimeToTimeEvent(any(TimeEvent.class))).thenReturn(timeEventOptional.get());

        ResponseEntity response = timeEventController.stopTimeEvent(userId);

        TimeEvent timeEventCreated = (TimeEvent) response.getBody();
        assertEquals(timeEventOptional.get(), timeEventCreated);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void ifTheTemplateToDeleteDoesNotExistA404IsReturned() {
        String id = "id";
        when(timeEventRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = timeEventController.deleteTimeEvent(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenDeletingATemplateTheResponseIsEmpty() {
        String eventIdToDelete = "id";
        TimeEvent eventToDelete = mock(TimeEvent.class);
        when(timeEventRepository.findById(eventIdToDelete)).thenReturn(Optional.of(eventToDelete));

        ResponseEntity<?> deletedTemplateResponse = timeEventController.deleteTimeEvent(eventIdToDelete);

        assertNull(deletedTemplateResponse.getBody());
        assertEquals(HttpStatus.NO_CONTENT, deletedTemplateResponse.getStatusCode());
        verify(timeEventRepository).delete(eventToDelete);
    }

    @Test
    void ifTheTemplateToUpdateDoesNotExistA404IsReturned() {
        String id = "id";
        when(timeEventRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = timeEventController.updateTimeEvent(id, mock(TimeEvent.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenATemplateIsUpdatedTheUpdatedTemplateIsReturned() {
        TimeEvent updatedEvent = mock(TimeEvent.class);
        Optional<TimeEvent> eventFound = Optional.of(mock(TimeEvent.class));
        String idTemplateToUpdate = "id";
        TimeEvent eventToUpdate = mock(TimeEvent.class);
        when(timeEventRepository.findById(idTemplateToUpdate)).thenReturn(eventFound);
        when(timeEventRepository.save(eventToUpdate)).thenReturn(updatedEvent);

        ResponseEntity<TimeEvent> updatedTemplateResponse = timeEventController.updateTimeEvent(idTemplateToUpdate, eventToUpdate);

        assertEquals(updatedEvent, updatedTemplateResponse.getBody());
        assertEquals(HttpStatus.OK, updatedTemplateResponse.getStatusCode());
        verify(timeEventRepository).save(eventToUpdate);
    }

    @Test
    void lastActiveTimeEventIsReturned() {
        String id = "someid";
        String orderByCriteria = "lastActive";
        Iterable<TimeEvent> lastTimeEvent = mock(Iterable.class);

        doReturn(lastTimeEvent).when(lastActiveEventService).getLastActiveTimeEvents( id,orderByCriteria, 1);
        ResponseEntity<Iterable> lastActiveTimeEventResponse = timeEventController.findTimeEvents(orderByCriteria, id, 1);

        assertEquals(lastTimeEvent, lastActiveTimeEventResponse.getBody());
        assertEquals(HttpStatus.OK, lastActiveTimeEventResponse.getStatusCode());

    }
}
