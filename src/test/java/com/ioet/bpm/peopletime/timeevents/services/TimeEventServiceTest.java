package com.ioet.bpm.peopletime.timeevents.services;

import com.google.common.collect.Iterables;
import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import com.ioet.bpm.peopletime.timetemplates.domain.TimeTemplate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeEventServiceTest {

    @Mock
    private TimeEventRepository timeEventRepository;

    @InjectMocks
    TimeEventService timeEventService;


    @Test
    void beforeStartingANewEventTheActiveEventsIsFound() {
        String userId = "lukas";

        timeEventService.init();
        timeEventService.createNewTimeEvent(mock(TimeTemplate.class), userId);

        verify(timeEventRepository, times(1)).findByStopTimeIsNullAndPersonId(userId);
    }

    @Test
    void whenStoppingATimeEventTheSavedTimeEventIsReturned() {
        TimeEvent timeEventToStop = mock(TimeEvent.class);
        TimeEvent stoppedTimeEvent = mock(TimeEvent.class);
        when(timeEventRepository.save(any(TimeEvent.class))).thenReturn(stoppedTimeEvent);

        TimeEvent savedTimeEvent = timeEventService.saveStopTimeToTimeEvent(timeEventToStop);

        assertEquals(stoppedTimeEvent, savedTimeEvent);
    }

    @Test
    void whenStoppingATimeEventTheStopTimeIsSet() {
        TimeEvent timeEventToStop = new TimeEvent();
        ArgumentCaptor<TimeEvent> savedEventCaptor = ArgumentCaptor.forClass(TimeEvent.class);

        timeEventService.saveStopTimeToTimeEvent(timeEventToStop);

        verify(timeEventRepository, times(1)).save(savedEventCaptor.capture());
        TimeEvent stoppedTimeEvent = savedEventCaptor.getValue();
        assertNotNull(stoppedTimeEvent.getStopTime());
    }

    @Test
    void whenLastActiveIsRequestedOnlyOneEventGetsReturned() {
        String personId = "someid";
        Iterable<TimeEvent> lastActiveTimeEvent = mock(Iterable.class);
        when(timeEventRepository.findLastActiveTimeEvent(personId)).thenReturn(lastActiveTimeEvent);

        Iterable lastActiveTimeEventFound = timeEventService.getLastActiveTimeEvents(personId, true);

        assertEquals(lastActiveTimeEvent, lastActiveTimeEventFound);
        verify(timeEventRepository).findLastActiveTimeEvent(personId);
    }

    @Test
    void whenLastActiveIsNotRequestedAllTimeEventsForOnePersonAreReturned() {
        String personId = "someid";
        Iterable<TimeEvent> lastActiveTimeEvent = mock(Iterable.class);
        when(timeEventRepository.findByPersonId(personId)).thenReturn(lastActiveTimeEvent);

        Iterable lastActiveTimeEventFound = timeEventService.getLastActiveTimeEvents(personId, false);

        assertEquals(lastActiveTimeEvent, lastActiveTimeEventFound);
        verify(timeEventRepository).findByPersonId(personId);
    }
}
