package com.osudpotro.posmaster.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("Generic not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
