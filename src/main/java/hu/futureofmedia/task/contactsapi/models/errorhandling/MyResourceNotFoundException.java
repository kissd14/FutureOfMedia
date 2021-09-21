package hu.futureofmedia.task.contactsapi.models.errorhandling;

public class MyResourceNotFoundException extends RuntimeException {
  public MyResourceNotFoundException(String message) {
    super(message);
  }
}
