package com.osudpotro.posmaster.tms.driver;

public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException() {
        super("Driver not found");
    }

    public DriverNotFoundException(String message) {
        super(message);
    }
}
