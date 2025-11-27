package com.osudpotro.posmaster.user;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("Duplicate found");
    }
    public DuplicateUserException(String message) {
        super(message);
    }
}
