package pl.coderslab.workshop2.user.exception;

public class UserExistsException extends RuntimeException {

    private static final String MESSAGE = "User already exists for given id: ";

    public UserExistsException(Long userId) {
        super(MESSAGE + userId);
    }
}
