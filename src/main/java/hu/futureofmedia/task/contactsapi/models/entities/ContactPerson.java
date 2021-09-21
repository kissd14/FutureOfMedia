package hu.futureofmedia.task.contactsapi.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.futureofmedia.task.contactsapi.models.enums.Status;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ContactPerson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String lastName;
  private String firstName;
  private String email;
  private String phoneNumber;
  @ManyToOne
  @JsonBackReference
  private Company company;
  private String note;
  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;
}
