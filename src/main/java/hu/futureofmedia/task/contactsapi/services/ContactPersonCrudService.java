package hu.futureofmedia.task.contactsapi.services;

import com.google.i18n.phonenumbers.NumberParseException;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;

public interface ContactPersonCrudService {
  ContactPersonsResponseDto getAll(Integer page, Integer pageSize);

  ContactPersonDto getById(Long id);

  void create(ContactPersonDto ContactPersonDto) throws NumberParseException;

  void update(ContactPersonDto ContactPersonDto, Long id) throws NumberParseException;

  void delete(Long id);
}
