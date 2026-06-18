package com.osudpotro.posmaster.common;

public class EntitySelfParentException extends RuntimeException {
    public EntitySelfParentException() {
        super("Self Entity found");
    }

    public EntitySelfParentException(String message) {
        super(message);
    }
}
