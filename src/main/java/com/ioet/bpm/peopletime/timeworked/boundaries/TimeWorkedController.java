package com.ioet.bpm.peopletime.timeworked.boundaries;

import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;
import com.ioet.bpm.peopletime.timeevents.repositories.TimeEventRepository;
import com.ioet.bpm.peopletime.timeevents.services.TimeEventService;
import com.ioet.bpm.peopletime.timetemplates.repositories.TimeTemplateRepository;
import com.ioet.bpm.peopletime.timeworked.domain.TimeWorked;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@AllArgsConstructor
@RestController
@RequestMapping("/time-worked")
@Api(value = "/time-worked", description = "Query Time Worked", produces = "application/json")
public class TimeWorkedController {

    private final TimeEventService timeEventService;

    @ApiOperation(value = "Query time worked", response = TimeWorked.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned time worked"),
            @ApiResponse(code = 404, message = "No working hours found yet")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable> getTimeWorked(@RequestParam(value = "personId") String personId) {
        Iterable<TimeEvent> timeEvents = (Iterable<TimeEvent>) timeEventService.getLastActiveTimeEvents(personId, false);
        long totalTimeWorked = 0;
        for (TimeEvent te : timeEvents) {
            if(te.getStopTime() != null) {
                totalTimeWorked += te.getStopTime().getTime() - te.getStartTime().getTime();
            }
        }
        ArrayList<TimeWorked> timeWorked = new ArrayList<>();
        timeWorked.add(new TimeWorked("all", totalTimeWorked));
        return new ResponseEntity<>(timeWorked, HttpStatus.OK);
    }
}
