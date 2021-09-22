package hu.futureofmedia.task.contactsapi.models.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContactPersonsResponseDto {
  private Integer page;
  private Integer pageSize;
  private Integer total;
  private List<ContactPersonResponseDto> contactPersonResponseDtoList;
}
