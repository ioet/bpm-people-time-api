package com.ioet.bpm.peopletime.timeevents.repositories;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface TimeEventRepository extends CrudRepository<TimeEvent, String>, CustomRepository {

    Iterable<TimeEvent> findByPersonId(String personId);

    Optional<TimeEvent> findByStopTimeIsNullAndPersonId(String userId);
}
