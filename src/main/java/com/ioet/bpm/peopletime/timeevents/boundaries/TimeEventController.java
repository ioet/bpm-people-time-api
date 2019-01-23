package com.ioet.bpm.peopletime.timeevents.boundaries;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.services.LastActiveEventService;
import com.ioet.bpm.peopletime.timetemplates.domain.TimeTemplate;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import com.ioet.bpm.peopletime.timetemplates.repositories.TimeTemplateRepository;
import com.ioet.bpm.peopletime.timeevents.services.TimeEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/time-events")
@Api(value = "/time-events", description = "Manage Time Events", produces = "application/json")
public class TimeEventController {

    private final TimeEventRepository timeEventRepository;
    private final TimeTemplateRepository timeTemplateRepository;
    private final TimeEventService timeEventService;
    private final LastActiveEventService lastActiveEventService;


    @ApiOperation(value = "Return a list of all events belonging to one person", response = TimeEvent.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all events belonging to one person")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable> findTimeEvents(@RequestParam(value = "personId") String personId,
                                                   @RequestParam(value = "orderBy", required = false) String orderByCriteria,
                                                   @RequestParam(value = "top", required = false) Integer top) {
        Iterable timeEvents = this.lastActiveEventService.getLastActiveTimeEvents(orderByCriteria, personId, top);
        return new ResponseEntity(timeEvents, HttpStatus.OK);
    }


    @ApiOperation(value = "Delete a time event")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the time event"),
            @ApiResponse(code = 404, message = "The time event to delete was not found")
    })
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TimeEvent> deleteTimeEvent(@PathVariable(value = "id") String timeEventId) {
        Optional<TimeEvent> timeEventOptional = timeEventRepository.findById(timeEventId);
        if (timeEventOptional.isPresent()) {
            timeEventRepository.delete(timeEventOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "Return the updated time event", response = TimeEvent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the time event"),
            @ApiResponse(code = 404, message = "The time event to update was not found")
    })
    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TimeEvent> updateTimeEvent(@PathVariable(value = "id") String timeEventId,
                                                     @Valid @RequestBody TimeEvent timeEventToUpdate) {

        Optional<TimeEvent> timeEventOptional = timeEventRepository.findById(timeEventId);
        if (timeEventOptional.isPresent()) {
            timeEventToUpdate.setId(timeEventOptional.get().getId());
            TimeEvent updatedTimeEvent = timeEventRepository.save(timeEventToUpdate);
            return new ResponseEntity<>(updatedTimeEvent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "Start a time-template -> create time-event", response = TimeEvent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the time event")
    })
    @PostMapping(path = "/start", produces = "application/json")
    public ResponseEntity startTimeEvent(@RequestParam(value = "templateId") String templateId, @RequestParam(value = "personId") String userId) {
        Optional<TimeTemplate> timeTemplateOptional = timeTemplateRepository.findById(templateId);
        if (timeTemplateOptional.isPresent()) {
            TimeEvent timeEventCreated = timeEventService.createNewTimeEvent(timeTemplateOptional.get(), userId);
            return new ResponseEntity<>(timeEventCreated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Template with id '" + templateId + "' not found.", HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation(value = "Stop a time-template -> set time-event stopTime", response = TimeEvent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully stopped the time event"),
            @ApiResponse(code = 404, message = "The time event to stop was not found")
    })
    @PostMapping(path = "/stop", produces = "application/json")
    public ResponseEntity<TimeEvent> stopTimeEvent(@RequestParam(value = "personId") String userId) {
        Optional<TimeEvent> timeEventToStopOptional = timeEventRepository.findByStopTimeIsNullAndPersonId(userId);
        if (timeEventToStopOptional.isPresent()) {
            TimeEvent stoppedTimeEvent = timeEventService.saveStopTimeToTimeEvent(timeEventToStopOptional.get());
            return new ResponseEntity<>(stoppedTimeEvent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
