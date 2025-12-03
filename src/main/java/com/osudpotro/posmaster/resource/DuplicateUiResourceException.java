package com.osudpotro.posmaster.resource;

public class DuplicateUiResourceException extends RuntimeException{
    public DuplicateUiResourceException() {
        super("UiResource already exists");
    }

    public DuplicateUiResourceException(String message) {
        super(message);
    }
}
