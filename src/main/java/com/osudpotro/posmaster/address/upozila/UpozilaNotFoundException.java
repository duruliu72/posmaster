package com.osudpotro.posmaster.address.upozila;


public class UpozilaNotFoundException extends RuntimeException {
    public UpozilaNotFoundException() {
        super("Branch not found");
    }

    public UpozilaNotFoundException(String message) {
        super(message);
    }
}