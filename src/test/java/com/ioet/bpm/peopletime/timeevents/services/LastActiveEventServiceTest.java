package com.ioet.bpm.peopletime.timeevents.services;

import com.google.common.collect.Iterables;
import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LastActiveEventServiceTest {

    @Mock
    private TimeEventRepository timeEventRepository;

    @InjectMocks
    LastActiveEventService lastActiveEventService;


    @Test
    public void whenTheOrderByCriteriaIsNullThenReturnAllEvents() {
        String personId = "someid";
        Iterable<TimeEvent> lastActiveTimeEvent = mock(Iterable.class);

        doReturn(lastActiveTimeEvent).when(timeEventRepository).findByPersonId(personId);
        Iterable lastActiveTimeEventFound = lastActiveEventService.getLastActiveTimeEvents(null, personId, 1);

        assertEquals(lastActiveTimeEvent, lastActiveTimeEventFound);
        verify(timeEventRepository).findByPersonId(personId);
    }

    @Test
    public void whenTheOrderByCriteriaIsNotNullThenReturnLastActiveEvent() {
        String personId = "someid";
        Optional<TimeEvent> lastActiveTimeEvent = Optional.of(mock(TimeEvent.class));

        doReturn(lastActiveTimeEvent).when(timeEventRepository).lastActiveTimeEvent(personId, 1);
        Iterable lastActiveTimeEventFound = lastActiveEventService.getLastActiveTimeEvents("lastActive", personId, 1);

        assertEquals(lastActiveTimeEvent.get(), lastActiveTimeEventFound.iterator().next());
        assertEquals(1, Iterables.size(lastActiveTimeEventFound));
        verify(timeEventRepository).lastActiveTimeEvent(personId, 1);
    }

    @Test(expected = RuntimeException.class)
    public void whenTheOrderByCriteriaIsUnknownThenReturnBadCriteriaMessage() {
        String personId = "someid";
        String badOrderByCriteria = "somecriteria";
        LastActiveEventService eventService = mock(LastActiveEventService.class);

        doThrow(new RuntimeException("Provide orderBy criteria not supported")).when(eventService).getLastActiveTimeEvents(badOrderByCriteria, personId, 1);

        eventService.getLastActiveTimeEvents(badOrderByCriteria, personId, 1);
    }
}
