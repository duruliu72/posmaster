package com.osudpotro.posmaster.dispatch;

public class DuplicateDispatchException extends RuntimeException {
    public DuplicateDispatchException() {
        super("Duplicate Purchase Invoice Found");
    }

    public DuplicateDispatchException(String message) {
        super(message);
    }
}
