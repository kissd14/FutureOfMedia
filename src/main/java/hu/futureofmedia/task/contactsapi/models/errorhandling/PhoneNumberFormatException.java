package hu.futureofmedia.task.contactsapi.models.errorhandling;

public class PhoneNumberFormatException extends RuntimeException{
  public PhoneNumberFormatException(String message) {
    super(message);
  }
}
