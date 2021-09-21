package hu.futureofmedia.task.contactsapi.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPersonResponseDto {
  private String name;
  private String companyName;
  private String email;
  private String phoneNumber;
}
