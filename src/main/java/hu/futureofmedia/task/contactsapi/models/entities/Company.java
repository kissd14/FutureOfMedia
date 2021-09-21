package hu.futureofmedia.task.contactsapi.models.entities;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Company {
  @Id
  private Long id;
  private String name;
  @OneToMany
  private Set<ContactPerson> contactPersons;
}
