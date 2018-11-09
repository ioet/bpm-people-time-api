package com.ioet.bpm.peopletime.boundaries;

import com.ioet.bpm.peopletime.domain.TimeTemplate;
import com.ioet.bpm.peopletime.repositories.TimeTemplateRepository;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@Tag("MyTests")
@ExtendWith(MockitoExtension.class)
class TimeTemplateControllerTest {

    @Mock
    private TimeTemplateRepository timeTemplateRepository;

    @InjectMocks
    private TimeTemplateController timeTemplateController;

    @Test
    void whenATemplateIsCreatedTheNewTemplateIsReturnedCorrectly() {
        TimeTemplate templateCreated = mock(TimeTemplate.class);
        TimeTemplate templateToCreate = new TimeTemplate();
        templateToCreate.setName("Test Name");
        templateToCreate.setPersonId("somePersonId");
        templateToCreate.setActivity("I did some things.");
        when(timeTemplateRepository.save(templateToCreate)).thenReturn(templateCreated);

        ResponseEntity<TimeTemplate> templateCreatedResponse = timeTemplateController.createTimeTemplate(templateToCreate);

        assertEquals(templateCreated, templateCreatedResponse.getBody());
        assertEquals(HttpStatus.CREATED, templateCreatedResponse.getStatusCode());
        verify(timeTemplateRepository, times(1)).save(templateToCreate);
    }

    @Test
    void theBodyContainsTheTemplatesForOnePersonFromTheRepository() {
        String personId = "userId";
        Iterable<TimeTemplate> templatesFound = mock(Iterable.class);
        when(timeTemplateRepository.findByPersonId(personId)).thenReturn(templatesFound);

        ResponseEntity<Iterable> existingTemplateResponse = timeTemplateController.getAllTimeTemplatesForOnePerson(personId);

        assertEquals(templatesFound, existingTemplateResponse.getBody());
        assertEquals(HttpStatus.OK, existingTemplateResponse.getStatusCode());
    }

    @Test
    void ifTheTemplateToDeleteDoesNotExistA404IsReturned() {
        String id = "id";
        when(timeTemplateRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = timeTemplateController.deleteTemplate(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenDeletingATemplateTheResponseIsEmpty() {
        String templateIdToDelete = "id";
        TimeTemplate templateToDelete = mock(TimeTemplate.class);
        when(timeTemplateRepository.findById(templateIdToDelete)).thenReturn(Optional.of(templateToDelete));

        ResponseEntity<?> deletedTemplateResponse = timeTemplateController.deleteTemplate(templateIdToDelete);

        assertNull(deletedTemplateResponse.getBody());
        assertEquals(HttpStatus.NO_CONTENT, deletedTemplateResponse.getStatusCode());
        verify(timeTemplateRepository, times(1)).delete(templateToDelete);
    }

    @Test
    void ifTheTemplateToUpdateDoesNotExistA404IsReturned() {
        String id = "id";
        when(timeTemplateRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = timeTemplateController.updateTemplate(id, mock(TimeTemplate.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenATemplateIsUpdatedTheUpdatedTemplateIsReturned() {
        TimeTemplate templateUpdated = mock(TimeTemplate.class);
        Optional<TimeTemplate> templateFound = Optional.of(mock(TimeTemplate.class));
        String idTemplateToUpdate = "id";
        TimeTemplate templateToUpdate = mock(TimeTemplate.class);
        when(timeTemplateRepository.findById(idTemplateToUpdate)).thenReturn(templateFound);
        when(timeTemplateRepository.save(templateToUpdate)).thenReturn(templateUpdated);

        ResponseEntity<TimeTemplate> updatedTemplateResponse = timeTemplateController.updateTemplate(idTemplateToUpdate, templateToUpdate);

        assertEquals(templateUpdated, updatedTemplateResponse.getBody());
        assertEquals(HttpStatus.OK, updatedTemplateResponse.getStatusCode());
        verify(timeTemplateRepository, times(1)).save(templateToUpdate);
    }
}
