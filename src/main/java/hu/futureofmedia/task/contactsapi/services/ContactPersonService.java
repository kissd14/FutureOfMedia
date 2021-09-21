package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ContactPersonService implements ContactPersonCrudService{
  @Override
  public ContactPersonsResponseDto getAll(Integer page, Integer pageSize) {
    return null;
  }

  @Override
  public ContactPersonDetailedResponseDto getById(Long id) {
    return null;
  }

  @Override
  public void create(ContactPersonInputDto contactPersonInputDto) {

  }

  @Override
  public void update(ContactPersonInputDto contactPersonInputDto) {

  }

  @Override
  public void delete(Long id) {

  }
}
