package com.osudpotro.posmaster.common;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException() {
        super("Duplicate Entity found");
    }
    public DuplicateEntityException(String message) {
        super(message);
    }
}