package com.ioet.bpm.peopletime.timeevents.services;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@AllArgsConstructor
@Service
public class LastActiveEventService {
    private final TimeEventRepository timeEventRepository;

    public Iterable<?> getLastActiveTimeEvents(String orderByCriteria, String personId, int top) {
        if (orderByCriteria == null) {
            return this.timeEventRepository.findByPersonId(personId);
        } else if ("lastActive".equals(orderByCriteria)) {
            ArrayList<TimeEvent> lastActiveTimeEvent = new ArrayList<>();
            lastActiveTimeEvent.add(this.timeEventRepository.lastActiveTimeEvent(personId, top).get());
            return lastActiveTimeEvent;
        } else {
            throw new RuntimeException("Provide orderBy criteria not supported");
        }

    }
}
