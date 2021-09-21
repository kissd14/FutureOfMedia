package hu.futureofmedia.task.contactsapi.models.errorhandling;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"status", "statusCode", "message", "errors", "timeStamp"})
public class ErrorResponse {

  private HttpStatus status;
  private String message;
  private List<String> errors;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  private final ZonedDateTime timeStamp = ZonedDateTime.now();

  @Builder
  public ErrorResponse(HttpStatus status, String message, List<String> errors) {
    this.status = status;
    this.message = message;
    this.errors = new ArrayList<>(errors);
  }

  public int getStatusCode() {
    return status.value();
  }

}
