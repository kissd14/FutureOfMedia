package hu.futureofmedia.task.contactsapi.controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonDto;
import hu.futureofmedia.task.contactsapi.models.dtos.ContactPersonsResponseDto;
import hu.futureofmedia.task.contactsapi.services.ContactPersonCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/contactperson")
public class ContactPersonController {
  private final ContactPersonCrudService contactPersonService;

  public ContactPersonController(
      ContactPersonCrudService contactPersonService) {
    this.contactPersonService = contactPersonService;
  }

  @Operation(summary = "Get record by id with detailed information",
      responses = {
          @ApiResponse(description = "The contact person",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ContactPersonDto.class))),
          @ApiResponse(responseCode = "404", description = "Contact person was not found"),
          @ApiResponse(responseCode = "400", description = "Contact person id was in wrong format, or index out of boundary")})
  @GetMapping("/{id}")
  public ResponseEntity<ContactPersonDto> getById(@PathVariable @Positive Long id) {
    return ResponseEntity.ok(contactPersonService.getById(id));
  }

  @Operation(summary = "Get contact persons by page number and page size",
      responses = {
          @ApiResponse(responseCode = "400", description = "A parameter value was invalid, or missing"),
          @ApiResponse(description = "The contact person list",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ContactPersonsResponseDto.class)))
      })
  @GetMapping()
  public ResponseEntity<ContactPersonsResponseDto> getAllOnThePage(
      @RequestParam @Min(value = 1) Integer page,
      @RequestParam(required = false)
      @Positive @Max(value = 50)
          Integer pageSize) {
    return ResponseEntity.ok(
        contactPersonService.getAll(page, Objects.requireNonNullElse(pageSize, 10)));
  }


  @Operation(summary = "Create contact person",
      responses = {
          @ApiResponse(responseCode = "204", description = "Contact person was created"),
          @ApiResponse(responseCode = "400", description = "A field value was invalid, or missing"),
          @ApiResponse(responseCode = "404", description = "Contact person, or company was not found")})
  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void create(@RequestBody @Valid ContactPersonDto ContactPersonDto)
      throws NumberParseException {
    contactPersonService.create(ContactPersonDto);
  }

  @Operation(summary = "Update contact person by id",
      responses = {
          @ApiResponse(responseCode = "204", description = "Contact person was updated"),
          @ApiResponse(responseCode = "400", description = "Parameter, or a field value was invalid, or missing"),
          @ApiResponse(responseCode = "404", description = "Contact person, or company was not found")})

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@RequestBody @Valid ContactPersonDto ContactPersonDto, @PathVariable
  @Positive Long id)
      throws NumberParseException {
    contactPersonService.update(ContactPersonDto, id);
  }

  @Operation(summary = "Delete contact person by id",
      responses = {
          @ApiResponse(responseCode = "204", description = "Contact person was deleted"),
          @ApiResponse(responseCode = "400", description = "Contact person id was invalid, or missing"),
          @ApiResponse(responseCode = "404", description = "Contact person was not found")})
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable @Positive Long id) {
    contactPersonService.delete(id);
  }
}
