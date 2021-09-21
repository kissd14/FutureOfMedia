package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import org.springframework.data.repository.CrudRepository;

public interface ContactPersonRepository extends CrudRepository<ContactPerson,Long> {

}
