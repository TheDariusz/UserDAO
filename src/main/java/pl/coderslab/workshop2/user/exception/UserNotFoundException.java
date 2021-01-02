package pl.coderslab.workshop2.user.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Failed to find user with email: ";

    public UserNotFoundException(Long userId) {
        super(MESSAGE + userId);
    }
}
