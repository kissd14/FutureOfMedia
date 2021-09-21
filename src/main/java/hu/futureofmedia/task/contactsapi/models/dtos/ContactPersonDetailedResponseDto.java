package hu.futureofmedia.task.contactsapi.models.dtos;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPersonDetailedResponseDto {

  private String firstName;
  private String lastName;
  private String companyName;
  private String email;
  private String phoneNumber;
  private String note;
  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;
}
