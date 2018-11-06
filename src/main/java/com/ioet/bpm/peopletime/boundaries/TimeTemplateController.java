package com.ioet.bpm.peopletime.boundaries;

import com.ioet.bpm.peopletime.domain.TimeTemplate;
import com.ioet.bpm.peopletime.repositories.TimeTemplateRepository;
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
@RequestMapping("/timetemplates")
@Api(value = "/timetemplates", description = "Manage Time Templates", produces = "application/json")
public class TimeTemplateController {

    private final TimeTemplateRepository timeTemplateRepository;

    @ApiOperation(value = "Return a list of all templates", response = TimeTemplate.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all templates")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable> getAllTimeTemplates() {
        Iterable<TimeTemplate> timeTemplates = this.timeTemplateRepository.findAll();
        return new ResponseEntity<>(timeTemplates, HttpStatus.OK);
    }

    @ApiOperation(value = "Return one template", response = TimeTemplate.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the template")
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TimeTemplate> getTimeTemplate(@PathVariable(value = "id") String templateId) {
        Optional<TimeTemplate> templateOptional = timeTemplateRepository.findById(templateId);
        return templateOptional.map(
                template -> new ResponseEntity<>(template, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Return the created template", response = TimeTemplate.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the template")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<TimeTemplate> createTimeTemplate(@RequestBody TimeTemplate template) {

        TimeTemplate templateCreated = timeTemplateRepository.save(template);
        return new ResponseEntity<>(templateCreated, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete a template")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the template"),
            @ApiResponse(code = 404, message = "The template to delete was not found")
    })
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TimeTemplate> deleteTemplate(@PathVariable(value = "id") String templateId) {
        Optional<TimeTemplate> template = timeTemplateRepository.findById(templateId);
        if (template.isPresent()) {
            timeTemplateRepository.delete(template.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Return the updated template", response = TimeTemplate.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the template"),
            @ApiResponse(code = 404, message = "The template to update was not found")
    })
    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TimeTemplate> updateTemplate(@PathVariable(value = "id") String templateId,
                                                       @Valid @RequestBody TimeTemplate templateToUpdate) {

        Optional<TimeTemplate> templateOptional = timeTemplateRepository.findById(templateId);
        if (templateOptional.isPresent()) {
            templateToUpdate.setId(templateOptional.get().getId());
            TimeTemplate updatedTemplate = timeTemplateRepository.save(templateToUpdate);
            return new ResponseEntity<>(updatedTemplate, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
