package com.osudpotro.posmaster.resource.api;

public class ApiResourceNotFoundException extends RuntimeException{
    public ApiResourceNotFoundException() {
        super("ApiResource not found");
    }

    public ApiResourceNotFoundException(String message) {
        super(message);
    }
}
