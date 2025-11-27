package com.osudpotro.posmaster.action;

public class ActionNotFoundException extends RuntimeException {
    public ActionNotFoundException() {
        super("Action not found");
    }
    public ActionNotFoundException(String message) {
        super(message);
    }

}