package hu.futureofmedia.task.contactsapi.controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.services.ContactPersonCrudService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contactperson")
public class ContactPersonController {
  private final ContactPersonCrudService contactPersonService;

  public ContactPersonController(
      ContactPersonCrudService contactPersonService) {
    this.contactPersonService = contactPersonService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody @Valid ContactPersonInputDto contactPersonInputDto)
      throws NumberParseException {
    contactPersonService.create(contactPersonInputDto);
  }
}
