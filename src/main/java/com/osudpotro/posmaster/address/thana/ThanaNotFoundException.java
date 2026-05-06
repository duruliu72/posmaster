package com.osudpotro.posmaster.address.thana;


public class ThanaNotFoundException extends RuntimeException {
    public ThanaNotFoundException() {
        super("Branch not found");
    }

    public ThanaNotFoundException(String message) {
        super(message);
    }
}