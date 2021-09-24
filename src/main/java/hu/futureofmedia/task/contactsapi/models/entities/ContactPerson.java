package hu.futureofmedia.task.contactsapi.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.futureofmedia.task.contactsapi.models.enums.Status;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact_persons")
@EqualsAndHashCode(exclude = {"createdAt","lastModifiedAt","phoneNumber"})
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
  @Enumerated(EnumType.STRING)
  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;
}
