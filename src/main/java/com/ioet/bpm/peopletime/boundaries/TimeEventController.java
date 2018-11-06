package com.ioet.bpm.peopletime.boundaries;

import com.ioet.bpm.peopletime.domain.TimeEvent;
import com.ioet.bpm.peopletime.repositories.TimeEventRepository;
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
@RequestMapping("/timeevents")
@Api(value = "/timeevents", description = "Manage Time Events", produces = "application/json")
public class TimeEventController {

    private final TimeEventRepository timeEventRepository;

    @ApiOperation(value = "Return a list of all time events", response = TimeEvent.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all time events")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable> getAllTimeEvents() {
        Iterable<TimeEvent> timeEvents = this.timeEventRepository.findAll();
        return new ResponseEntity<>(timeEvents, HttpStatus.OK);
    }

    @ApiOperation(value = "Return one time event", response = TimeEvent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the time event")
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TimeEvent> getTimeEvent(@PathVariable(value = "id") String eventId) {
        Optional<TimeEvent> eventOptional = timeEventRepository.findById(eventId);
        return eventOptional.map(
                event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Return the created time event", response = TimeEvent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the time event")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<TimeEvent> createTimeEvent(@RequestBody TimeEvent timeEvent) {

        TimeEvent timeEventCreated = timeEventRepository.save(timeEvent);
        return new ResponseEntity<>(timeEventCreated, HttpStatus.CREATED);
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
}
