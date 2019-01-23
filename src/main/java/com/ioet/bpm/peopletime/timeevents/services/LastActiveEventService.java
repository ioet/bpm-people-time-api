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

    public Iterable<?> getLastActiveTimeEvents(String orderByCriteria, String personId, Integer top) {
        if (orderByCriteria == null) {
            return this.timeEventRepository.findByPersonId(personId);
        } else if ("lastActive".equals(orderByCriteria)) {
            Optional eventsFoundWithId = this.timeEventRepository.findById(personId);
            if (eventsFoundWithId.isPresent()) {
                ArrayList<TimeEvent> lastActiveTimeEvent = new ArrayList<>();
                if (top == null) {
                    top = 1;
                }
                Optional<TimeEvent> timeEvent = this.timeEventRepository.findLastActiveTimeEvent(personId, top);
                timeEvent.ifPresent(lastActiveTimeEvent::add);
                return lastActiveTimeEvent;
            } else {
                throw new RuntimeException("No person found whit the provided ID");
            }

        } else {
            throw new RuntimeException("Provide orderBy criteria not supported");
        }

    }
}
