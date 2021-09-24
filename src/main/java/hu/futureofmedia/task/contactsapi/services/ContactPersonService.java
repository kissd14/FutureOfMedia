package hu.futureofmedia.task.contactsapi.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import hu.futureofmedia.task.contactsapi.models.entities.Company;
import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import hu.futureofmedia.task.contactsapi.models.enums.Status;
import hu.futureofmedia.task.contactsapi.models.errorhandling.PhoneNumberFormatException;
import hu.futureofmedia.task.contactsapi.models.errorhandling.ResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactPersonRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactPersonService implements ContactPersonCrudService {
  private final ContactPersonRepository contactPersonRepository;
  private final CompanyRepository companyRepository;
  private final PhoneNumberUtil phoneUtil;

  public ContactPersonService(
      ContactPersonRepository contactPersonRepository,
      CompanyRepository companyRepository,
      PhoneNumberUtil phoneUtil) {
    this.contactPersonRepository = contactPersonRepository;
    this.companyRepository = companyRepository;
    this.phoneUtil = phoneUtil;
  }

  private ContactPerson convertToEntity(ContactPersonInputDto contactPersonInputDto)
      throws NumberParseException {


    Phonenumber.PhoneNumber phoneNumber =
        phoneUtil.parse(contactPersonInputDto.getPhoneNumber(), "ZZ");

    if (!phoneUtil.isValidNumber(phoneNumber)) {
      throw new PhoneNumberFormatException("This phone number is not valid.");
    }

    String formattedPhoneNumber =
        phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

    if (!companyRepository.findCompanyById(contactPersonInputDto.getCompanyId()).isPresent()) {
      throw new ResourceNotFoundException(
          "There is no company with id: " + contactPersonInputDto.getCompanyId());
    }

    Company company = companyRepository.findCompanyById(contactPersonInputDto.getCompanyId()).get();
    return new ContactPerson(
        null,
        contactPersonInputDto.getLastName(),
        contactPersonInputDto.getFirstName(),
        contactPersonInputDto.getEmail(),
        formattedPhoneNumber,
        company,
        contactPersonInputDto.getNote(),
        Status.ACTIVE,
        LocalDateTime.now(),
        LocalDateTime.now());
  }

  private ContactPersonDetailedResponseDto convertToContactPersonDetailedResponseDto(
      ContactPerson contactPerson) {
    return new ContactPersonDetailedResponseDto(
        contactPerson.getFirstName(),
        contactPerson.getLastName(),
        contactPerson.getCompany().getName(),
        contactPerson.getEmail(),
        contactPerson.getPhoneNumber(),
        contactPerson.getNote(),
        contactPerson.getCreatedAt(),
        contactPerson.getLastModifiedAt());
  }

  private ContactPersonResponseDto convertToContactPersonResponseDto(ContactPerson contactPerson) {
    return new ContactPersonResponseDto(
        contactPerson.getId(),
        contactPerson.getLastName() + " " + contactPerson.getFirstName(),
        contactPerson.getCompany().getName(),
        contactPerson.getEmail(),
        contactPerson.getPhoneNumber());
  }

  @Override
  @Transactional(readOnly = true)
  public ContactPersonsResponseDto getAll(Integer page, Integer pageSize) {
    List<ContactPersonResponseDto> contactPersonResponseDtoList = new ArrayList<>();
    List<ContactPerson> contactPersonList =
        contactPersonRepository.getAllByPageAndPageSize(PageRequest.of(page - 1, pageSize));
    for (ContactPerson contactPerson : contactPersonList) {
      ContactPersonResponseDto contactPersonResponseDto =
          convertToContactPersonResponseDto(contactPerson);
      contactPersonResponseDtoList.add(contactPersonResponseDto);
    }
    return new ContactPersonsResponseDto(
        page,
        pageSize,
        contactPersonList.size(),
        contactPersonResponseDtoList);
  }


  @Override
  @Transactional(readOnly = true)
  public ContactPersonDetailedResponseDto getById(Long id) {
    if (!contactPersonRepository.findByIdAndStatusActive(id).isPresent()) {
      throw new ResourceNotFoundException("There is no contact person with id: " + id + ".");
    }
    ContactPerson contactPerson = contactPersonRepository.findByIdAndStatusActive(id).get();
    ContactPersonDetailedResponseDto contactPersonDetailedResponseDto =
        convertToContactPersonDetailedResponseDto(contactPerson);
    return contactPersonDetailedResponseDto;
  }

  @Override
  @Transactional
  public void create(ContactPersonInputDto contactPersonInputDto) throws NumberParseException {
    ContactPerson contactPerson = convertToEntity(contactPersonInputDto);
    contactPersonRepository.save(contactPerson);
  }

  @Override
  @Transactional
  public void update(ContactPersonInputDto contactPersonInputDto, Long id)
      throws NumberParseException {
    if (!contactPersonRepository.findByIdAndStatusActive(id).isPresent()) {
      throw new ResourceNotFoundException("There is no contact person with id: " + id + ".");
    }
    ContactPerson contactPerson = contactPersonRepository.findByIdAndStatusActive(id).get();
    ContactPerson contactPersonToSave = convertToEntity(contactPersonInputDto);
    contactPersonToSave.setId(id);
    contactPersonToSave.setCreatedAt(contactPerson.getCreatedAt());
    contactPersonRepository.save(contactPersonToSave);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (!contactPersonRepository.findByIdAndStatusActive(id).isPresent()) {
      throw new ResourceNotFoundException("There is no contact person with id: " + id + ".");
    }
    ContactPerson contactPerson = contactPersonRepository.findByIdAndStatusActive(id).get();
    contactPerson.setStatus(Status.DELETED);
    contactPersonRepository.save(contactPerson);
  }
}
