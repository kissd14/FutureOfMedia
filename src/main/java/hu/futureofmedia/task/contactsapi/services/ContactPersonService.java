package hu.futureofmedia.task.contactsapi.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDto;
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


  private ContactPerson setContactPersonProperties(
      ContactPerson contactPerson, ContactPersonDto contactPersonDto) throws NumberParseException {
    if (!companyRepository.findCompanyById(contactPersonDto.getCompanyId()).isPresent()) {
      throw new ResourceNotFoundException(
          "There is no company with id: " + contactPersonDto.getCompanyId());
    }
    Phonenumber.PhoneNumber phoneNumber =
        phoneUtil.parse(contactPersonDto.getPhoneNumber(), "ZZ");

    if (!phoneUtil.isValidNumber(phoneNumber)) {
      throw new PhoneNumberFormatException("This phone number is not valid.");
    }

    String formattedPhoneNumber =
        phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

    contactPerson.setFirstName(contactPersonDto.getFirstName());
    contactPerson.setLastName(contactPersonDto.getLastName());
    contactPerson.setCompany(
        companyRepository.findCompanyById(contactPersonDto.getCompanyId()).get());
    contactPerson.setEmail(contactPersonDto.getEmail());
    contactPerson.setPhoneNumber(formattedPhoneNumber);
    contactPerson.setNote(contactPersonDto.getNote());
    contactPerson.setStatus(Status.ACTIVE);
    return contactPerson;
  }

  private ContactPersonDto convertToContactPersonDto(ContactPerson contactPerson){
    if (!companyRepository.findCompanyById(contactPerson.getCompany().getId()).isPresent()) {
      throw new ResourceNotFoundException(
          "There is no company with id: " + contactPerson.getCompany().getId());
    }
    return new ContactPersonDto(
        contactPerson.getId(),
        contactPerson.getFirstName(),
        contactPerson.getLastName(),
        contactPerson.getCompany().getId(),
        contactPerson.getCompany().getName(),
        contactPerson.getEmail(),
        contactPerson.getPhoneNumber(),
        contactPerson.getNote(),
        contactPerson.getCreatedAt(),
        contactPerson.getLastModifiedAt()
    );
  }

  @Override
  @Transactional(readOnly = true)
  public ContactPersonsResponseDto getAll(Integer page, Integer pageSize) {
    List<ContactPersonDto> ContactPersonDtoList = new ArrayList<>();
    List<ContactPerson> contactPersonList =
        contactPersonRepository.getAllByPageAndPageSize(PageRequest.of(page - 1, pageSize));
    for (ContactPerson contactPerson : contactPersonList) {
      ContactPersonDto ContactPersonDto =
          convertToContactPersonDto(contactPerson);
      ContactPersonDtoList.add(ContactPersonDto);
    }
    return new ContactPersonsResponseDto(
        page,
        pageSize,
        contactPersonList.size(),
        ContactPersonDtoList);
  }


  @Override
  @Transactional(readOnly = true)
  public ContactPersonDto getById(Long id) {
    if (!contactPersonRepository.findByIdAndStatusActive(id).isPresent()) {
      throw new ResourceNotFoundException("There is no contact person with id: " + id + ".");
    }
    ContactPerson contactPerson = contactPersonRepository.findByIdAndStatusActive(id).get();
    ContactPersonDto contactPersonDto =
        convertToContactPersonDto(contactPerson);
    return contactPersonDto;
  }

  @Override
  @Transactional
  public void create(ContactPersonDto ContactPersonDto) throws NumberParseException {
    ContactPerson contactPerson = setContactPersonProperties(new ContactPerson(), ContactPersonDto);
    contactPerson.setId(null);
    contactPerson.setCreatedAt(LocalDateTime.now());
    contactPerson.setLastModifiedAt(LocalDateTime.now());
    contactPersonRepository.save(contactPerson);
  }

  @Override
  @Transactional
  public void update(ContactPersonDto contactPersonDto, Long id)
      throws NumberParseException {
    if (!contactPersonRepository.findByIdAndStatusActive(id).isPresent()) {
      throw new ResourceNotFoundException("There is no contact person with id: " + id + ".");
    }
    ContactPerson contactPerson = contactPersonRepository.findByIdAndStatusActive(id).get();
    setContactPersonProperties(contactPerson, contactPersonDto);
    contactPerson.setLastModifiedAt(LocalDateTime.now());
    contactPersonRepository.save(contactPerson);
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
