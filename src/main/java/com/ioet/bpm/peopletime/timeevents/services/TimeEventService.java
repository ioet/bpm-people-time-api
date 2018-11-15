package com.ioet.bpm.peopletime.timeevents.services;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timetemplates.domain.TimeTemplate;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@AllArgsConstructor
@Service
public class TimeEventService {

    private final TimeEventRepository timeEventRepository;

    public TimeEvent createNewTimeEvent(TimeTemplate timeTemplateToStart, String userId) {
        stopCurrentActiveTimeEventIfPresent(userId);
        TimeEvent timeEvent = new TimeEvent(timeTemplateToStart, userId);
        return timeEventRepository.save(timeEvent);
    }

    private void stopCurrentActiveTimeEventIfPresent(String userId) {
        Optional<TimeEvent> currentlyRunningTimeEvent = timeEventRepository.findByStopTimeIsNullAndPersonId(userId);
        currentlyRunningTimeEvent.ifPresent(this::saveStopTimeToTimeEvent);
    }

    public TimeEvent saveStopTimeToTimeEvent(TimeEvent timeEventToStop) {
        timeEventToStop.setStopTime(new Date());
        return timeEventRepository.save(timeEventToStop);
    }
}
