package hu.futureofmedia.task.contactsapi;

import hu.futureofmedia.task.contactsapi.services.ContactPersonCrudService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactPersonController {
  private final ContactPersonCrudService contactPersonService;

  public ContactPersonController(
      ContactPersonCrudService contactPersonService) {
    this.contactPersonService = contactPersonService;
  }
}
