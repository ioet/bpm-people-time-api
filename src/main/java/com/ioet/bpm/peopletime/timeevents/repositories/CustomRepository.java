package com.ioet.bpm.peopletime.timeevents.repositories;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;

import java.util.Optional;

public interface CustomRepository {
    Optional<TimeEvent> lastActiveTimeEvent(String personId);
}

