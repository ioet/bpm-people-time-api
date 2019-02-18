package com.ioet.bpm.peopletime.activities.boundaries;

import com.ioet.bpm.peopletime.activities.domain.Activity;
import com.ioet.bpm.peopletime.activities.repositories.ActivityRepository;
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
@RequestMapping("/activities")
@Api(value = "/activities", description = "Manage Activities", produces = "application/json")
public class ActivityController {

    private final ActivityRepository activityRepository;

    @ApiOperation(value = "Return a list of all activities ", response = Activity.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all activities")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable> getAllActivities() {
        Iterable<Activity> activities = activityRepository.findAll();
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @ApiOperation(value = "Return a list of all activities ", response = Activity.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all activities")
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Activity> getActivityById(@PathVariable(value = "id") String id) {
        Optional<Activity> activityOptional = activityRepository.findById(id);
        return activityOptional.map(
                activity -> new ResponseEntity<>(activity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Return the created activity", response = Activity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the activity")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<Activity> createActivity(@Valid @RequestBody Activity activity) {
        Activity activityCreated = activityRepository.save(activity);
        return new ResponseEntity<>(activityCreated, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete an activity")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the activity"),
            @ApiResponse(code = 404, message = "The activity to delete was not found")
    })
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Activity> deleteActivity(@PathVariable(value = "id") String activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isPresent()) {
            activityRepository.delete(activity.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Return the updated activity", response = Activity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the activity"),
            @ApiResponse(code = 404, message = "The activity to update was not found")
    })
    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Activity> updateActivity(@PathVariable(value = "id") String activityId,
                                                   @Valid @RequestBody Activity activityToUpdate) {

        Optional<Activity> activityOptional = activityRepository.findById(activityId);
        if (activityOptional.isPresent()) {
            activityToUpdate.setId(activityOptional.get().getId());
            Activity updatedActivity = activityRepository.save(activityToUpdate);
            return new ResponseEntity<>(updatedActivity, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
