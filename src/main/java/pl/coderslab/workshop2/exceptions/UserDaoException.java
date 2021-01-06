package pl.coderslab.workshop2.exceptions;

public class UserDaoException extends RuntimeException {

  public UserDaoException(String message) {
    super(message);
  }

  public UserDaoException(String message, Throwable e) {
    super(message, e);
  }
}
