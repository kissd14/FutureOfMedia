package hu.futureofmedia.task.contactsapi.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPersonInputDto {
  @NotEmpty(message = "stringValue has to be present")
  private String firstName;
  @NotEmpty(message = "stringValue has to be present")
  private String lastName;
  @Positive(message = "longValue has to be positive")
  @NotNull(message = "longValue has to be present")
  private Long companyId;
  @NotEmpty(message = "stringValue has to be present")
  @Email(message = "stringValue has to have email format")
  private String email;
  @NotEmpty(message = "stringValue has to be present")
  private String phoneNumber;
  private String note;
}
