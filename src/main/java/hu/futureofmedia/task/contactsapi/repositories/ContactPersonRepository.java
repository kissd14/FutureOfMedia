package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ContactPersonRepository extends CrudRepository<ContactPerson, Long> {
  @Query(value = "SELECT * FROM contact_persons c WHERE id=?1 and status='ACTIVE'", nativeQuery = true)
  Optional<ContactPerson> findByIdAndStatusActive(Long id);
}
