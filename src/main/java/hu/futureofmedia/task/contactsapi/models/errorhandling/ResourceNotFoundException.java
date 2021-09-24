package hu.futureofmedia.task.contactsapi.models.errorhandling;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
