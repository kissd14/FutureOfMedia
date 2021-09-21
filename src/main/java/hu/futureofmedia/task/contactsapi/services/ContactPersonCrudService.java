package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;

public interface ContactPersonCrudService {
  ContactPersonsResponseDto getAll(Integer page, Integer pageSize);

  ContactPersonDetailedResponseDto getById(Long id);

  void create(ContactPersonInputDto contactPersonInputDto);

  void update(ContactPersonInputDto contactPersonInputDto);

  void delete(Long id);
}
