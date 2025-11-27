package com.osudpotro.posmaster.resource;

public class ApiResourceNotFoundException extends RuntimeException{
    public ApiResourceNotFoundException() {
        super("ApiResource not found");
    }

    public ApiResourceNotFoundException(String message) {
        super(message);
    }
}
