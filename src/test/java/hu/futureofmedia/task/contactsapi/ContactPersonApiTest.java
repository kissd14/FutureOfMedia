package hu.futureofmedia.task.contactsapi;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import hu.futureofmedia.task.contactsapi.controllers.ContactPersonController;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDetailedResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonInputDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonResponseDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import hu.futureofmedia.task.contactsapi.models.entities.Company;
import hu.futureofmedia.task.contactsapi.models.entities.ContactPerson;
import hu.futureofmedia.task.contactsapi.models.enums.Status;
import hu.futureofmedia.task.contactsapi.models.errorhandling.MyResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.services.ContactPersonCrudService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ContactPersonController.class)
public class ContactPersonApiTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ContactPersonCrudService contactPersonService;
  private ContactPerson contactPerson1;
  private ContactPerson contactPerson2;
  private ContactPerson contactPerson3;
  private ContactPerson contactPerson3Updated;
  private Company company1;
  private Company company2;
  private Company company3;
  private ContactPersonsResponseDto contactPersonsResponseDto;
  private List<ContactPerson> contactPersonList;
  private List<ContactPersonResponseDto> contactPersonResponseDtoList;

  @BeforeEach
  public void setUp() {
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
    this.contactPersonResponseDtoList = new ArrayList<>();
    contactPersonResponseDtoList.add(contactPersonResponseDto3);
    contactPersonResponseDtoList.add(contactPersonResponseDto1);
    contactPersonResponseDtoList.add(contactPersonResponseDto2);
    this.contactPersonsResponseDto =
        new ContactPersonsResponseDto(1, 10, 3, contactPersonResponseDtoList);
    this.contactPersonList = new ArrayList<>();
    contactPersonList.add(contactPerson3);
    contactPersonList.add(contactPerson1);
    contactPersonList.add(contactPerson2);
  }

  @DisplayName("getAllOnThePage Should return 3 contact persons in alphabetic order with page 1, pageSize 10 and total 3")
  @Test
  public void getAllOnThePageShouldResponseOkAndReturnWith3ContactPersonsInAlphabeticOrder()
      throws Exception {
    Mockito.when(contactPersonService.getAll(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(contactPersonsResponseDto);
    mockMvc.perform(get("/api/contactperson?page=1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("page", is(1)))
        .andExpect(jsonPath("pageSize", is(10)))
        .andExpect(jsonPath("total", is(3)))
        .andExpect(jsonPath("contactPersonResponseDtoList[0].id", is(3)))
        .andExpect(jsonPath("contactPersonResponseDtoList[0].name", is("Ana Big")))
        .andExpect(jsonPath("contactPersonResponseDtoList[0].companyName", is("Company#3")))
        .andExpect(jsonPath("contactPersonResponseDtoList[0].email", is("anabig@gmail.com")))
        .andExpect(jsonPath("contactPersonResponseDtoList[0].phoneNumber", is("+36707416892")))
        .andExpect(jsonPath("contactPersonResponseDtoList[1].id", is(1)))
        .andExpect(jsonPath("contactPersonResponseDtoList[1].name", is("John Doe")))
        .andExpect(jsonPath("contactPersonResponseDtoList[1].companyName", is("Company#1")))
        .andExpect(jsonPath("contactPersonResponseDtoList[1].email", is("johndeo@gmail.com")))
        .andExpect(jsonPath("contactPersonResponseDtoList[1].phoneNumber", is("+36701234567")))
        .andExpect(jsonPath("contactPersonResponseDtoList[2].id", is(2)))
        .andExpect(jsonPath("contactPersonResponseDtoList[2].name", is("John Smith")))
        .andExpect(jsonPath("contactPersonResponseDtoList[2].companyName", is("Company#2")))
        .andExpect(jsonPath("contactPersonResponseDtoList[2].email", is("johnsmith@gmail.com")))
        .andExpect(jsonPath("contactPersonResponseDtoList[2].phoneNumber", is("+36208536458")));

  }

  @DisplayName("getAllOnThePage should return 400, because of missing parameter")
  @Test
  public void getAllOnThePageShouldResponseBadRequestMissingParameter()
      throws Exception {
    mockMvc.perform(get("/api/contactperson"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getAllOnThePage should return 400, because of invalid (string) parameter")
  @Test
  public void getAllOnThePageShouldResponseBadRequestInvalidParameterString()
      throws Exception {
    mockMvc.perform(get("/api/contactperson?page=alma"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getAllOnThePage should return 400, because of invalid (negative number) parameter")
  @Test
  public void getAllOnThePageShouldResponseBadRequestInvalidParameterNegativeNumber()
      throws Exception {
    mockMvc.perform(get("/api/contactperson?page=-1"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getAllOnThePage should return 400, because of invalid (string) parameter")
  @Test
  public void getAllOnThePageShouldResponseBadRequestInvalidParameterPageSizeString()
      throws Exception {
    mockMvc.perform(get("/api/contactperson?page=1&pageSize=alma"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getAllOnThePage should return 400, because of invalid (negative number) parameter")
  @Test
  public void getAllOnThePageShouldResponseBadRequestInvalidParameterPageSizeNegativeNumber()
      throws Exception {
    mockMvc.perform(get("/api/contactperson?page=1&pageSize=-1"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getAllOnThePage should return 400, because of invalid (too big number) parameter")
  @Test
  public void getAllOnThePageShouldResponseBadRequestInvalidParameterPageSizeTooBigNumber()
      throws Exception {
    mockMvc.perform(get("/api/contactperson?page=1&pageSize=51"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getById should return 200, and the detailed contact person info")
  @Test
  public void getByIdShouldReturnContactPerson()
      throws Exception {
    ContactPersonDetailedResponseDto contactPersonDetailedResponseDto =
        new ContactPersonDetailedResponseDto(contactPerson1.getFirstName(),
            contactPerson1.getLastName(), contactPerson1.getCompany().getName(),
            contactPerson1.getEmail(), contactPerson1.getPhoneNumber(), contactPerson1.getNote(),
            contactPerson1.getCreatedAt(), contactPerson1.getLastModifiedAt());
    Mockito.when(contactPersonService.getById(Mockito.anyLong()))
        .thenReturn(contactPersonDetailedResponseDto);
    mockMvc.perform(get("/api/contactperson/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("firstName", is("Doe")))
        .andExpect(jsonPath("lastName", is("John")))
        .andExpect(jsonPath("companyName", is("Company#1")))
        .andExpect(jsonPath("email", is("johndeo@gmail.com")))
        .andExpect(jsonPath("phoneNumber", is("+36701234567")))
        .andExpect(jsonPath("note", is("great at listening")));
  }

  @DisplayName("getByID should return 400, because of invalid (string) parameter")
  @Test
  public void getByIdShouldResponseBadRequestInvalidParameterString()
      throws Exception {
    mockMvc.perform(get("/api/contactperson/alma"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getByID should return 400, because of invalid (negative number) parameter")
  @Test
  public void getByIdShouldResponseBadRequestInvalidParameterNegativeNumber()
      throws Exception {
    mockMvc.perform(get("/api/contactperson/-1"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getByID should return 400, because of missing parameter")
  @Test
  public void getByIdShouldResponseBadRequestMissingParameter()
      throws Exception {
    mockMvc.perform(get("/api/contactperson/"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("getByID should return 404, because of the provided id parameter doesn't exist in the database")
  @Test
  public void getByIdShouldResponseNotFound()
      throws Exception {
    Mockito.when(contactPersonService.getById(Mockito.anyLong())).thenThrow(
        MyResourceNotFoundException.class);
    mockMvc.perform(get("/api/contactperson/1"))
        .andExpect(status().isNotFound());
  }

  @DisplayName("create should return 204")
  @Test
  public void createShouldCreateContactPersonAndRespondNoContent() throws Exception {
    mockMvc.perform(post("/api/contactperson")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"companyId\":1," +
                    "\"email\":\"johndoe@gmail.com\",\"phoneNumber\":\"+361234567\",\"note\":\"it's a note\"}"))
        .andExpect(status().isNoContent());

  }

  @DisplayName("update should return 204")
  @Test
  public void updateShouldUpdateContactPersonAndRespondNoContent() throws Exception {
    mockMvc.perform(put("/api/contactperson/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"companyId\":1," +
                    "\"email\":\"johndoe@gmail.com\",\"phoneNumber\":\"+361234567\",\"note\":\"it's a note\"}"))
        .andExpect(status().isNoContent());
  }

  @DisplayName("update should return 404, because of the provided id parameter doesn't exist in the database")
  @Test
  public void updateShouldResponseNotFound() throws Exception {
    Mockito.doThrow(new MyResourceNotFoundException("")).when(contactPersonService)
        .update(Mockito.any(ContactPersonInputDto.class), Mockito.anyLong());
    mockMvc.perform(put("/api/contactperson/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"companyId\":1," +
                    "\"email\":\"johndoe@gmail.com\",\"phoneNumber\":\"+361234567\",\"note\":\"it's a note\"}"))
        .andExpect(status().isNotFound());
  }

  @DisplayName("delete should return 204")
  @Test
  public void deleteShouldDeleteContactPersonAndRespondNoContent() throws Exception {
    mockMvc.perform(delete("/api/contactperson/1"))
        .andExpect(status().isNoContent());
  }

  @DisplayName("delete should return 404, because of the provided id parameter doesn't exist in the database")
  @Test
  public void deleteShouldResponseNotFound() throws Exception {
    Mockito.doThrow(new MyResourceNotFoundException("")).when(contactPersonService)
        .delete(Mockito.anyLong());
    mockMvc.perform(delete("/api/contactperson/1"))
        .andExpect(status().isNotFound());
  }
}
