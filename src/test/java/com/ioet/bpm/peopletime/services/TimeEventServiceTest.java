package com.ioet.bpm.peopletime.services;

import com.ioet.bpm.peopletime.domain.TimeEvent;
import com.ioet.bpm.peopletime.domain.TimeTemplate;
import com.ioet.bpm.peopletime.repositories.TimeEventRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Tag("MyTests")
@ExtendWith(MockitoExtension.class)
class TimeEventServiceTest {

    @Mock
    private TimeEventRepository timeEventRepository;

    @InjectMocks
    TimeEventService timeEventService;


    @Test
    void beforeStartingANewEventTheActiveEventsIsFound() {
        String userId = "lukas";

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
}
