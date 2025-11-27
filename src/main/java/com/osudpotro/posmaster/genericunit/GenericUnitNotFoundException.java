package com.osudpotro.posmaster.genericunit;

public class GenericUnitNotFoundException extends RuntimeException {
    public GenericUnitNotFoundException(){
        super("Generic Unit not found");
    }
    public GenericUnitNotFoundException(String message) {
        super(message);
    }
}