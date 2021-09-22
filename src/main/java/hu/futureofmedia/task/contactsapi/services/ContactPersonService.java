package hu.futureofmedia.task.contactsapi.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import hu.futureofmedia.task.contactsapi.models.entities.Company;
import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import hu.futureofmedia.task.contactsapi.models.enums.Status;
import hu.futureofmedia.task.contactsapi.models.errorhandling.MyResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.models.errorhandling.PhoneNumberFormatException;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactPersonRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class ContactPersonService implements ContactPersonCrudService {
  private final ContactPersonRepository contactPersonRepository;
  private final CompanyRepository companyRepository;

  public ContactPersonService(
      ContactPersonRepository contactPersonRepository,
      CompanyRepository companyRepository) {
    this.contactPersonRepository = contactPersonRepository;
    this.companyRepository = companyRepository;
  }

  private ContactPerson convertToEntity(ContactPersonInputDto contactPersonInputDto,
                                        Company company) throws NumberParseException {

    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    Phonenumber.PhoneNumber phoneNumber =
        phoneUtil.parse(contactPersonInputDto.getPhoneNumber(), "ZZ");

    if (!phoneUtil.isValidNumber(phoneNumber)) {
      throw new PhoneNumberFormatException("This phone number is not valid.");
    }

    String formattedPhoneNumber =
        phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

    return new ContactPerson(null, contactPersonInputDto.getLastName(),
        contactPersonInputDto.getFirstName(), contactPersonInputDto.getEmail(),
        formattedPhoneNumber, company, contactPersonInputDto.getNote(),
        Status.ACTIVE,
        LocalDateTime.now(), LocalDateTime.now());
  }

  @Override
  public ContactPersonsResponseDto getAll(Integer page, Integer pageSize) {
    return null;
  }


  @Override
  public ContactPersonDetailedResponseDto getById(Long id) {
    return null;
  }

  @Override
  public void create(ContactPersonInputDto contactPersonInputDto) throws NumberParseException {
    if (!companyRepository.findCompanyById(contactPersonInputDto.getCompanyId()).isPresent()) {
      throw new MyResourceNotFoundException(
          "There is no company with id: " + contactPersonInputDto.getCompanyId());
    }
    Company company = companyRepository.findCompanyById(contactPersonInputDto.getCompanyId()).get();
    ContactPerson contactPerson = convertToEntity(contactPersonInputDto, company);
    contactPersonRepository.save(contactPerson);
  }

  @Override
  public void update(ContactPersonInputDto contactPersonInputDto) {

  }

  @Override
  public void delete(Long id) {

  }
}
