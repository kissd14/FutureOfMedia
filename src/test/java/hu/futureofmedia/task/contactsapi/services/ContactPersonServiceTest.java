package hu.futureofmedia.task.contactsapi.services;

import static org.mockito.Mockito.times;


import com.google.i18n.phonenumbers.NumberParseException;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import hu.futureofmedia.task.contactsapi.models.entities.Company;
import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import hu.futureofmedia.task.contactsapi.models.enums.Status;
import hu.futureofmedia.task.contactsapi.models.errorhandling.MyResourceNotFoundException;
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

  private Company company1;
  private Company company2;
  private Company company3;

  private ContactPerson contactPerson1;
  private ContactPerson contactPerson2;
  private ContactPerson contactPerson3;
  private ContactPerson contactPerson3Updated;
  private ContactPerson contactPerson3Deleted;

  @BeforeEach
  public void setUp() {
    this.companyRepository = Mockito.mock(CompanyRepository.class);
    this.contactPersonRepository = Mockito.mock(ContactPersonRepository.class);
    this.contactPersonService =
        new ContactPersonService(contactPersonRepository, companyRepository);
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
    ContactPersonResponseDto contactPersonResponseDto1 =
        new ContactPersonResponseDto(contactPerson1.getId(),
            contactPerson1.getLastName() + " " + contactPerson1.getFirstName(),
            contactPerson1.getCompany().getName(), contactPerson1.getEmail(),
            contactPerson1.getPhoneNumber());
    ContactPersonResponseDto contactPersonResponseDto2 =
        new ContactPersonResponseDto(contactPerson2.getId(),
            contactPerson2.getLastName() + " " + contactPerson2.getFirstName(),
            contactPerson2.getCompany().getName(), contactPerson2.getEmail(),
            contactPerson2.getPhoneNumber());
    ContactPersonResponseDto contactPersonResponseDto3 =
        new ContactPersonResponseDto(contactPerson3.getId(),
            contactPerson3.getLastName() + " " + contactPerson3.getFirstName(),
            contactPerson3.getCompany().getName(), contactPerson3.getEmail(),
            contactPerson3.getPhoneNumber());
    List<ContactPersonResponseDto> contactPersonResponseDtoList = new ArrayList<>();
    contactPersonResponseDtoList.add(contactPersonResponseDto1);
    contactPersonResponseDtoList.add(contactPersonResponseDto2);
    contactPersonResponseDtoList.add(contactPersonResponseDto3);
    ContactPersonsResponseDto contactPersonsResponseDto =
        new ContactPersonsResponseDto(1, 10, 3, contactPersonResponseDtoList);
    List<ContactPerson> contactPersonList = new ArrayList<>();
    contactPersonList.add(contactPerson1);
    contactPersonList.add(contactPerson2);
    contactPersonList.add(contactPerson3);
    Mockito.when(contactPersonRepository.getAllByPageAndPageSize(Mockito.any()))
        .thenReturn(contactPersonList);
    Assertions.assertEquals(contactPersonsResponseDto, contactPersonService.getAll(1, 10));
  }

  @Test
  @DisplayName("getByID should return with the correct DTO")
  void getByIdShouldReturnWithTheCorrectDTO() {
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.of(contactPerson1));
    ContactPersonDetailedResponseDto contactPersonDetailedResponseDto =
        new ContactPersonDetailedResponseDto(contactPerson1.getFirstName(),
            contactPerson1.getLastName(), contactPerson1.getCompany().getName(),
            contactPerson1.getEmail(), contactPerson1.getPhoneNumber(), contactPerson1.getNote(),
            contactPerson1.getCreatedAt(), contactPerson1.getLastModifiedAt());
    Assertions.assertEquals(contactPersonService.getById(1L), contactPersonDetailedResponseDto);
  }

  @Test
  @DisplayName("getByID should throw MyResourceNotFoundException")
  void getByIdShouldThrowMyResourceNotFoundException() {
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(MyResourceNotFoundException.class,
        () -> contactPersonService.getById(123L));
  }

  @Test
  @DisplayName("create should throw MyResourceNotFoundException")
  void createShouldThrowMyResourceNotFoundException() {
    ContactPersonInputDto contactPersonInputDto =
        new ContactPersonInputDto(contactPerson1.getFirstName(), contactPerson1.getLastName(),
            123L, contactPerson1
            .getEmail(), contactPerson1.getPhoneNumber(), contactPerson1.getNote());
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong())).thenReturn(Optional.empty());
    Assertions.assertThrows(MyResourceNotFoundException.class,
        () -> contactPersonService.create(contactPersonInputDto));
  }

  @Test
  @DisplayName("create should create contact person")
  void createShouldCreateContactPerson() throws NumberParseException {
    ContactPersonInputDto contactPersonInputDto =
        new ContactPersonInputDto(contactPerson3.getFirstName(), contactPerson3.getLastName(),
            contactPerson3.getCompany().getId(), contactPerson3
            .getEmail(), contactPerson3.getPhoneNumber(), contactPerson3.getNote());
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong()))
        .thenReturn(Optional.of(company3));
    contactPersonService.create(contactPersonInputDto);
    Mockito.verify(contactPersonRepository, times(1)).save(contactPerson3);
  }

  @Test
  @DisplayName("update should throw MyResourceNotFoundException")
  void updateShouldThrowMyResourceNotFoundExceptionBecauseOfCompany() {
    ContactPersonInputDto contactPersonInputDto =
        new ContactPersonInputDto(contactPerson1.getFirstName(), contactPerson1.getLastName(),
            123L, contactPerson1
            .getEmail(), contactPerson1.getPhoneNumber(), contactPerson1.getNote());
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong())).thenReturn(Optional.empty());
    Assertions.assertThrows(MyResourceNotFoundException.class,
        () -> contactPersonService.update(contactPersonInputDto, 12L));
  }

  @Test
  @DisplayName("update should throw MyResourceNotFoundException")
  void updateShouldThrowMyResourceNotFoundExceptionBecauseOfContactPerson() {
    ContactPersonInputDto contactPersonInputDto =
        new ContactPersonInputDto(contactPerson1.getFirstName(), contactPerson1.getLastName(),
            1L, contactPerson1
            .getEmail(), contactPerson1.getPhoneNumber(), contactPerson1.getNote());
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    Assertions.assertThrows(MyResourceNotFoundException.class,
        () -> contactPersonService.update(contactPersonInputDto, 12L));
  }

  @Test
  @DisplayName("update should update contact person")
  void updateShouldUpdateContactPerson() throws NumberParseException {
    ContactPersonInputDto contactPersonInputDto =
        new ContactPersonInputDto(contactPerson3.getFirstName(), "Anna",
            contactPerson3.getCompany().getId(), contactPerson3
            .getEmail(), contactPerson3.getPhoneNumber(), contactPerson3.getNote());
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.of(contactPerson3));
    Mockito.when(companyRepository.findCompanyById(Mockito.anyLong()))
        .thenReturn(Optional.of(company3));
    contactPersonService.update(contactPersonInputDto, 3L);
    Mockito.verify(contactPersonRepository, times(1)).save(contactPerson3Updated);
  }

  @Test
  @DisplayName("delete should throw MyResourceNotFoundException")
  void deleteShouldThrowMyResourceNotFoundException() {
    Mockito.when(contactPersonRepository.findByIdAndStatusActive(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    Assertions.assertThrows(MyResourceNotFoundException.class,
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