package hu.futureofmedia.task.contactsapi.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ContactPersonsResponseDto {
  @Schema(description = "Index of the page")
  private Integer page;
  @Schema(description = "Number of the maximum items on the page")
  private Integer pageSize;
  @Schema(description = "Number of the items on the page")
  private Integer total;
  @Schema(description = "List of the contact persons on the page")
  private List<ContactPersonResponseDto> contactPersonResponseDtoList;
}
