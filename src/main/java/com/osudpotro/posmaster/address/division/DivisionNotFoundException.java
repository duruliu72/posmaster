package com.osudpotro.posmaster.address.division;


public class DivisionNotFoundException extends RuntimeException {
    public DivisionNotFoundException() {
        super("Branch not found");
    }

    public DivisionNotFoundException(String message) {
        super(message);
    }
}