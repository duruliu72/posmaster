package com.osudpotro.posmaster.tms.driver;

public class DuplicateDriverException extends RuntimeException{
    public DuplicateDriverException() {
        super("Driver already exists");
    }

    public DuplicateDriverException(String message) {
        super(message);
    }
}
