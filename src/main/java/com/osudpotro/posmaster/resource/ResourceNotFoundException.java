package com.osudpotro.posmaster.resource;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException() {
        super("UiResource not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
