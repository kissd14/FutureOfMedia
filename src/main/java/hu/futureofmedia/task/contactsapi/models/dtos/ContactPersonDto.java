package hu.futureofmedia.task.contactsapi.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ContactPersonDto {
  @Positive
  @Schema(description = "Contact person's id", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;
  @NotEmpty
  @Schema(description = "Contact person's first name")
  private String firstName;
  @NotEmpty
  @Schema(description = "Contact person's last name")
  private String lastName;
  @Positive
  @NotNull
  @Schema(description = "Contact person's company's primary key (id)")
  private Long companyId;
  @Schema(description = "Contact person's company's name")
  private String companyName;
  @Email
  @NotEmpty
  @Schema(description = "Contact person's email", accessMode = Schema.AccessMode.READ_ONLY)
  private String email;
  @Schema(description = "Contact person's phone number")
  private String phoneNumber;
  @Schema(description = "Some note for contact person")
  private String note;
  @Schema(description = "Date of user creation", accessMode = Schema.AccessMode.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  private LocalDateTime createdAt;
  @Schema(description = "Date of user's last modification", accessMode = Schema.AccessMode.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  private LocalDateTime lastModifiedAt;
}
