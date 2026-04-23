package com.osudpotro.posmaster.common;


public class EntityException extends RuntimeException {
    public EntityException() {
        super("Entity Exception found");
    }

    public EntityException(String message) {
        super(message);
    }
}