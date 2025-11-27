package com.osudpotro.posmaster.generic;

public class GenericNotFoundException extends RuntimeException{
    public GenericNotFoundException() {
        super("Generic not found");
    }

    public GenericNotFoundException(String message) {
        super(message);
    }
}