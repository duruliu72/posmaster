package com.osudpotro.posmaster.dispatch;

public class DispatchException extends RuntimeException {
    public DispatchException() {
        super("Duplicate Purchase Invoice Found");
    }

    public DispatchException(String message) {
        super(message);
    }
}
