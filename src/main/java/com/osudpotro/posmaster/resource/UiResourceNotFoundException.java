package com.osudpotro.posmaster.resource;

public class UiResourceNotFoundException extends RuntimeException{
    public UiResourceNotFoundException() {
        super("UiResource not found");
    }

    public UiResourceNotFoundException(String message) {
        super(message);
    }
}
