package com.ioet.bpm.peopletime.repositories;

import com.ioet.bpm.peopletime.domain.TimeEvent;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface TimeEventRepository extends CrudRepository<TimeEvent, String> {

}
