package hu.futureofmedia.task.contactsapi.models.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPersonInputDto {
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;
  @Positive
  @NotNull
  private Long companyId;
  @NotEmpty
  private String email;
  private String phoneNumber;
  private String note;
}
