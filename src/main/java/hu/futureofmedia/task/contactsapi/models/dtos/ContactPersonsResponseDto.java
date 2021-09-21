package hu.futureofmedia.task.contactsapi.models.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPersonsResponseDto {
  private Integer page;
  private Integer pageSize;
  private Integer total;
  private List<ContactPersonResponseDto> contactPersonResponseDtoList;
}
