package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ContactPersonRepository extends CrudRepository<ContactPerson, Long> {
  @Query(value = "SELECT * FROM contact_persons c WHERE id=?1 and status='ACTIVE'", nativeQuery = true)
  Optional<ContactPerson> findByIdAndStatusActive(Long id);

  @Query(value = "SELECT * FROM contact_persons c WHERE status='ACTIVE' ORDER BY c.first_name", nativeQuery = true)
  List<ContactPerson> getAllByPageAndPageSize(Pageable pageable);

  @Query(value = "SELECT Count(*) FROM contact_persons",nativeQuery = true)
  Integer countAll();
}
