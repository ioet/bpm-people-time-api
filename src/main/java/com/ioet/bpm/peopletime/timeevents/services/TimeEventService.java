package com.ioet.bpm.peopletime.timeevents.services;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import com.ioet.bpm.peopletime.timetemplates.domain.TimeTemplate;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class TimeEventService {

    private DozerBeanMapper mapper;
    private final TimeEventRepository timeEventRepository;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
        mapper.addMapping(new BeanMappingBuilder() {

            @Override
            protected void configure() {
                mapping(TimeTemplate.class, TimeEvent.class, TypeMappingOptions.mapNull(false));
            }
        });
    }

    public TimeEvent createNewTimeEvent(TimeTemplate timeTemplateToStart, String userId) {
        stopCurrentActiveTimeEventIfPresent(userId);

        TimeEvent timeEvent = new TimeEvent(userId);
        mapper.map(timeTemplateToStart, timeEvent);

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

    public Iterable<?> getLastActiveTimeEvents(String personId, boolean lastActive) {
        Iterable<TimeEvent> response;
        if (lastActive) {
            response = timeEventRepository.findLastActiveTimeEvent(personId);
        } else {
            response = timeEventRepository.findByPersonId(personId);
        }
        return response;
    }
}
