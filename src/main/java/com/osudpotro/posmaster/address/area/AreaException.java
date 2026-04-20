package com.osudpotro.posmaster.address.area;


public class AreaException extends RuntimeException {
    public AreaException() {
        super("Area Exception found");
    }

    public AreaException(String message) {
        super(message);
    }
}