package hu.futureofmedia.task.contactsapi.models.errorhandling;

public class MyResourceAlreadyExistException extends RuntimeException {
  public MyResourceAlreadyExistException(String message) {
    super(message);
  }
}
