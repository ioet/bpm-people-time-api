package com.ioet.bpm.peopletime.timeevents.repositories;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;

import java.util.Optional;

public interface CustomRepository {

    Iterable<TimeEvent> findLastActiveTimeEvent(String personId);

}
