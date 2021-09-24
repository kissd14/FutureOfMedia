package hu.futureofmedia.task.contactsapi.services;

import static org.mockito.Mockito.times;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import hu.futureofmedia.task.contactsapi.models.entities.Company;
import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import hu.futureofmedia.task.contactsapi.models.enums.Status;
import hu.futureofmedia.task.contactsapi.models.errorhandling.ResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactPersonRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ContactPersonServiceTest {
  private CompanyRepository companyRepository;
  private ContactPersonRepository contactPersonRepository;
  private ContactPersonCrudService contactPersonService;
  private PhoneNumberUtil phoneNumberUtil;

  private Company company1;
  private Company company2;
  private Company company3;

  private ContactPerson contactPerson1;
  private ContactPerson contactPerson2;
  private ContactPerson contactPerson3;
  private ContactPerson contactPerson3Create;
  private ContactPerson contactPerson3Updated;
  private ContactPerson contactPerson3Deleted;

  @BeforeEach
  public void setUp() {
    this.phoneNumberUtil = Mockito.mock(PhoneNumberUtil.class);
    this.companyRepository = Mockito.mock(CompanyRepository.class);
    this.contactPersonRepository = Mockito.mock(ContactPersonRepository.class);
    this.contactPersonService =
        new ContactPersonService(contactPersonRepository, companyRepository, phoneNumberUtil);
    this.company1 = new Company(1L, "Company#1");
    this.company2 = new Company(2L, "Company#2");
    this.company3 = new Company(3L, "Company#3");

    this.contactPerson1 =
        new ContactPerson(1L, "John", "Doe", "johndeo@gmail.com", "+36701234567", company1,
            "great at listening", Status.ACTIVE,
            LocalDateTime.now(), LocalDateTime.now());
    this.contactPerson2 =
        new ContactPerson(2L, "John", "Smith", "johnsmith@gmail.com", "+36208536458", company2,
            "fast learner", Status.ACTIVE,
            LocalDateTime.now(), LocalDateTime.now());
    this.contactPerson3 =
        new ContactPerson(3L, "Ana", "Big", "anabig@gmail.com", "+36707416892", company3,
            "unreachable on mondays", Status.ACTIVE,
            LocalDateTime.now(), LocalDateTime.now());
    this.contactPerson3Create =
        new ContactPerson(null, "Ana", "Big", "anabig@gmail.com", "+36707416892", company3,
            "unreachable on mondays", Status.ACTIVE,
            LocalDateTime.now(), LocalDateTime.now());
    this.contactPerson3Updated =
        new ContactPerson(3L, "Anna", "Big", "anabig@gmail.com", "+36707416892", company3,
            "unreachable on mondays", Status.ACTIVE,
            LocalDateTime.now(), LocalDateTime.now());
    this.contactPerson3Deleted =
        new ContactPerson(3L, "Ana", "Big", "anabig@gmail.com", "+36707416892", company3,
            "unreachable on mondays", Status.DELETED,
            LocalDateTime.now(), LocalDateTime.now());
  }

  @Test
  @DisplayName("getAll should return the first 3 records in alphabetic order")
  void getAllShouldReturn3Records() {
    ContactPersonDto ContactPersonDto1 =
        new ContactPersonDto(
            contactPerson1.getId(),
            contactPerson1.getFirstName(),
            contactPerson1.getLastName(),
            contactPerson1.getCompany().getId(),
            contactPerson1.getCompany().getName(),
            contactPerson1.getEmail(),
            contactPerson1.getPhoneNumber(),
            contactPerson1.getNote(),
            contactPerson1.getCreatedAt(),
            contactPerson1.getLastModifiedAt());
    ContactPersonDto ContactPersonDto2 =
        new ContactPersonDto(
            contactPerson2.getId(),
            contactPerson2.getFirstName(),
            contactPerson2.getLastName(),
            contactPerson2.getCompany().getId(),
            contactPerson2.getCompany().getName(),
            contactPerson2.getEmail(),
            contactPerson2.getPhoneNumber(),
            contactPerson2.getNote(),
            contactPerson2.getCreatedAt(),
            contactPerson2.getLastModifiedAt());

    ContactPersonDto ContactPersonDto3 =
        new ContactPersonDto(
            contactPerson3.getId(),
            contactPerson3.getFirstName(),
            contactPerson3.getLastName(),
            contactPerson3.getCompany().getId(),
            contactPerson3.getCompany().getName(),
            contactPerson3.getEmail(),
            contactPerson3.getPhoneNumber(),
            contactPerson3.getNote(),
            contactPerson3.getCreatedAt(),
            contactPerson3.getLastModifiedAt());

    List<ContactPersonDto> contactPersonDtoList = new ArrayList<>();
    contactPersonDtoList.add(ContactPersonDto1);
    contactPersonDtoList.add(ContactPersonDto2);
    contactPersonDtoList.add(ContactPersonDto3);
    ContactPersonsResponseDto contactPersonsResponseDto =
        new ContactPersonsResponseDto(1, 10, 3, contactPersonDtoList);
    List<ContactPerson> contactPersonList = new ArrayList<>();
    contactPersonList.add(contactPerson1);
    contactPersonList.add(contactPerson2);
    contactPersonList.add(contactPerson3);
    Mockito.when(contactPersonRepository.getAllByPageAndPageSize(Mockito.any()))
        .thenReturn(contactPersonList);
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong())).thenReturn(Optional.of(company1));
    Assertions.assertEquals(contactPersonsResponseDto, contactPersonService.getAll(1, 10));
  }

  @Test
  @DisplayName("getByID should return with the correct DTO")
  void getByIdShouldReturnWithTheCorrectDTO() {
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.of(contactPerson1));
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong())).thenReturn(Optional.of(company1));
    ContactPersonDto contactPersonDto =
        new ContactPersonDto(
            contactPerson1.getId(),
            contactPerson1.getFirstName(),
            contactPerson1.getLastName(),
            contactPerson1.getCompany().getId(),
            contactPerson1.getCompany().getName(),
            contactPerson1.getEmail(),
            contactPerson1.getPhoneNumber(),
            contactPerson1.getNote(),
            contactPerson1.getCreatedAt(),
            contactPerson1.getLastModifiedAt());
    Assertions.assertEquals(contactPersonService.getById(1L), contactPersonDto);
  }

  @Test
  @DisplayName("getByID should throw MyResourceNotFoundException")
  void getByIdShouldThrowMyResourceNotFoundException() {
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(ResourceNotFoundException.class,
        () -> contactPersonService.getById(123L));
  }

  @Test
  @DisplayName("create should throw MyResourceNotFoundException")
  void createShouldThrowMyResourceNotFoundException() {
    ContactPersonDto ContactPersonDto =
        new ContactPersonDto(
            contactPerson1.getId(),
            contactPerson1.getFirstName(),
            contactPerson1.getLastName(),
            contactPerson1.getCompany().getId(),
            contactPerson1.getCompany().getName(),
            contactPerson1.getEmail(),
            contactPerson1.getPhoneNumber(),
            contactPerson1.getNote(),
            contactPerson1.getCreatedAt(),
            contactPerson1.getLastModifiedAt());
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong())).thenReturn(Optional.empty());
    Assertions.assertThrows(ResourceNotFoundException.class,
        () -> contactPersonService.create(ContactPersonDto));
  }

  @Test
  @DisplayName("create should create contact person")
  void createShouldCreateContactPerson() throws NumberParseException {
    ContactPersonDto contactPersonDto =
        new ContactPersonDto(
            contactPerson3.getId(),
            contactPerson3.getFirstName(),
            contactPerson3.getLastName(),
            contactPerson3.getCompany().getId(),
            contactPerson3.getCompany().getName(),
            contactPerson3.getEmail(),
            contactPerson3.getPhoneNumber(),
            contactPerson3.getNote(),
            contactPerson3.getCreatedAt(),
            contactPerson3.getLastModifiedAt());
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong()))
        .thenReturn(Optional.of(company3));
    Mockito.when(phoneNumberUtil.isValidNumber(Mockito.any())).thenReturn(true);
    contactPersonService.create(contactPersonDto);
    Mockito.verify(contactPersonRepository, times(1)).save(contactPerson3Create);
  }

  @Test
  @DisplayName("update should throw MyResourceNotFoundException")
  void updateShouldThrowMyResourceNotFoundExceptionBecauseOfCompany() {
    ContactPersonDto ContactPersonDto =
        new ContactPersonDto(
            contactPerson1.getId(),
            contactPerson1.getFirstName(),
            contactPerson1.getLastName(),
            contactPerson1.getCompany().getId(),
            contactPerson1.getCompany().getName(),
            contactPerson1.getEmail(),
            contactPerson1.getPhoneNumber(),
            contactPerson1.getNote(),
            contactPerson1.getCreatedAt(),
            contactPerson1.getLastModifiedAt());
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong())).thenReturn(Optional.empty());
    Assertions.assertThrows(ResourceNotFoundException.class,
        () -> contactPersonService.update(ContactPersonDto, 12L));
  }

  @Test
  @DisplayName("update should throw MyResourceNotFoundException")
  void updateShouldThrowMyResourceNotFoundExceptionBecauseOfContactPerson() {
    ContactPersonDto ContactPersonDto =
        new ContactPersonDto(
            contactPerson1.getId(),
            contactPerson1.getFirstName(),
            contactPerson1.getLastName(),
            contactPerson1.getCompany().getId(),
            contactPerson1.getCompany().getName(),
            contactPerson1.getEmail(),
            contactPerson1.getPhoneNumber(),
            contactPerson1.getNote(),
            contactPerson1.getCreatedAt(),
            contactPerson1.getLastModifiedAt());
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    Assertions.assertThrows(ResourceNotFoundException.class,
        () -> contactPersonService.update(ContactPersonDto, 12L));
  }

  @Test
  @DisplayName("update should update contact person")
  void updateShouldUpdateContactPerson() throws NumberParseException {
    ContactPersonDto ContactPersonDto =
        new ContactPersonDto(
            contactPerson3.getId(),
            contactPerson3.getFirstName(),
            "Anna",
            contactPerson3.getCompany().getId(),
            contactPerson3.getCompany().getName(),
            contactPerson3.getEmail(),
            contactPerson3.getPhoneNumber(),
            contactPerson3.getNote(),
            contactPerson3.getCreatedAt(),
            contactPerson3.getLastModifiedAt());
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.of(contactPerson3));
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong()))
        .thenReturn(Optional.of(company3));
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong()))
        .thenReturn(Optional.of(company3));
    Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
    phoneNumber.setCountryCode(36);
    phoneNumber.setRawInput("+36704064785");
    Mockito.when(phoneNumberUtil.parse(Mockito.any(),Mockito.any())).thenReturn(phoneNumber);
    Mockito.when(phoneNumberUtil.isValidNumber(Mockito.any())).thenReturn(true);
    contactPerson3Updated.setPhoneNumber(phoneNumber.toString());
    contactPersonService.update(ContactPersonDto, 3L);
    Mockito.verify(contactPersonRepository, times(1)).save(contactPerson3Updated);
  }

  @Test
  @DisplayName("delete should throw MyResourceNotFoundException")
  void deleteShouldThrowMyResourceNotFoundException() {
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    Assertions.assertThrows(ResourceNotFoundException.class,
        () -> contactPersonService.delete(123L));
  }

  @Test
  @DisplayName("delete should delete contact person")
  void deleteShouldDeleteContactPerson() {
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.of(contactPerson3));
    contactPersonService.delete(1L);
    Mockito.verify(contactPersonRepository, times(1)).save(contactPerson3Deleted);
  }
}