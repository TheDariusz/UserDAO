package pl.coderslab.workshop2.exceptions;

public class EmailDuplicateException extends RuntimeException {
  public EmailDuplicateException(String message) {
    super(message);
  }
}
