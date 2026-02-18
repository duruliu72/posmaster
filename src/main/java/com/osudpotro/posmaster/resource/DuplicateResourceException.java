package com.osudpotro.posmaster.resource;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException() {
        super("Resource already exists");
    }

    public DuplicateResourceException(String message) {
        super(message);
    }
}
