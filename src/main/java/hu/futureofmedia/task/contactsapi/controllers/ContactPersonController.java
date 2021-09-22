package hu.futureofmedia.task.contactsapi.controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import hu.futureofmedia.task.contactsapi.services.ContactPersonCrudService;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/contactperson")
public class ContactPersonController {
  private final ContactPersonCrudService contactPersonService;

  public ContactPersonController(
      ContactPersonCrudService contactPersonService) {
    this.contactPersonService = contactPersonService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ContactPersonDetailedResponseDto> getById(@PathVariable @Positive Long id) {
    return ResponseEntity.ok(contactPersonService.getById(id));
  }

  @GetMapping()
  public ResponseEntity<ContactPersonsResponseDto> getAllOnThePage(
      @RequestParam @Min(value = 1) Integer page,
      @RequestParam(required = false)
      @Positive @Max(value = 50)
          Integer pageSize) {
    return ResponseEntity.ok(
        contactPersonService.getAll(page, Objects.requireNonNullElse(pageSize, 10)));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody @Valid ContactPersonInputDto contactPersonInputDto)
      throws NumberParseException {
    contactPersonService.create(contactPersonInputDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@RequestBody @Valid ContactPersonInputDto contactPersonInputDto, @PathVariable
  @Positive Long id)
      throws NumberParseException {
    contactPersonService.update(contactPersonInputDto, id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable @Positive Long id) {
    contactPersonService.delete(id);
  }
}
