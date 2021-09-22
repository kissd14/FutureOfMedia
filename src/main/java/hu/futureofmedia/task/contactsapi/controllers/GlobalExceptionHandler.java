package hu.futureofmedia.task.contactsapi.controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import hu.futureofmedia.task.contactsapi.models.errorhandling.ErrorResponse;
import hu.futureofmedia.task.contactsapi.models.errorhandling.MyResourceAlreadyExistException;
import hu.futureofmedia.task.contactsapi.models.errorhandling.MyResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.models.errorhandling.PhoneNumberFormatException;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(badRequest).body(
        ErrorResponse.builder()
            .status(badRequest)
            .message("Validation error")
            .errors(
                exception.getBindingResult().getFieldErrors().stream()
                    .map(i -> i.getField() + ": " + i.getDefaultMessage())
                    .collect(Collectors.toList())
            )
            .build()
    );
  }

  @ExceptionHandler(NumberParseException.class)
  public ResponseEntity<ErrorResponse> handleNumberParseException(
      NumberParseException exception) {

    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(badRequest).body(
        ErrorResponse.builder()
            .status(badRequest)
            .message("The number is not considered as a possible number.")
            .errors(Collections.emptyList())
            .build()
    );
  }

  @ExceptionHandler(PhoneNumberFormatException.class)
  public ResponseEntity<ErrorResponse> handlePhoneNumberFormatException(
      PhoneNumberFormatException exception) {

    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(badRequest).body(
        ErrorResponse.builder()
            .status(badRequest)
            .message(exception.getMessage())
            .errors(Collections.emptyList())
            .build()
    );
  }

  @ExceptionHandler(MyResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(
      MyResourceNotFoundException exception) {

    HttpStatus notFound = HttpStatus.NOT_FOUND;
    return ResponseEntity.status(notFound).body(
        ErrorResponse.builder()
            .status(notFound)
            .message(exception.getMessage())
            .errors(Collections.emptyList())
            .build()
    );
  }


  @ExceptionHandler(MyResourceAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(
      MyResourceAlreadyExistException exception) {

    HttpStatus conflict = HttpStatus.CONFLICT;
    return ResponseEntity.status(conflict).body(
        ErrorResponse.builder()
            .status(conflict)
            .message(exception.getMessage())
            .errors(Collections.emptyList())
            .build()
    );

  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllUnhandledException(
      Exception exception) {

    HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(internalServerError).body(
        ErrorResponse.builder()
            .status(internalServerError)
            .message("Internal server error.")
            .errors(Collections.emptyList())
            .build()
    );

  }
}