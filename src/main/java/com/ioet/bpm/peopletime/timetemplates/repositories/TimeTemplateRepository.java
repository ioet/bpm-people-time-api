package com.ioet.bpm.peopletime.timetemplates.repositories;

import com.ioet.bpm.peopletime.timetemplates.domain.TimeTemplate;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface TimeTemplateRepository extends CrudRepository<TimeTemplate, String> {

    Iterable<TimeTemplate> findByPersonId(String personId);
}
