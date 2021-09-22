package hu.futureofmedia.task.contactsapi.services;

import com.google.i18n.phonenumbers.NumberParseException;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;

public interface ContactPersonCrudService {
  ContactPersonsResponseDto getAll(Integer page, Integer pageSize);

  ContactPersonDetailedResponseDto getById(Long id);

  void create(ContactPersonInputDto contactPersonInputDto) throws NumberParseException;

  void update(ContactPersonInputDto contactPersonInputDto);

  void delete(Long id);
}
