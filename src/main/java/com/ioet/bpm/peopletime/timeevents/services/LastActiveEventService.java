package com.ioet.bpm.peopletime.timeevents.services;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LastActiveEventService {
    private final TimeEventRepository timeEventRepository;

    public Iterable<?> getLastActiveTimeEvents(String personId, boolean lastActive) {
        if (lastActive) {
            ArrayList<TimeEvent> lastActiveTimeEvent = new ArrayList<>();
            Optional<TimeEvent> timeEvent = this.timeEventRepository.findLastActiveTimeEvent(personId);
            timeEvent.ifPresent(lastActiveTimeEvent::add);
            return lastActiveTimeEvent;
        } else {
            return timeEventRepository.findByPersonId(personId);
        }
    }
}
