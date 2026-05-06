package com.osudpotro.posmaster.address.area;


public class AreaNotFoundException extends RuntimeException {
    public AreaNotFoundException() {
        super("Branch not found");
    }

    public AreaNotFoundException(String message) {
        super(message);
    }
}