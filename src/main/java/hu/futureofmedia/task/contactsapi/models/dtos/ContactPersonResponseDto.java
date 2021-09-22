package hu.futureofmedia.task.contactsapi.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContactPersonResponseDto {
  private Long id;
  private String name;
  private String companyName;
  private String email;
  private String phoneNumber;
}
