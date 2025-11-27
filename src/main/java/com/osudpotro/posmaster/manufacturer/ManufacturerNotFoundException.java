package com.osudpotro.posmaster.manufacturer;

public class ManufacturerNotFoundException extends RuntimeException {
    public ManufacturerNotFoundException(){
        super("Manufacturer not Found");
    }
    public ManufacturerNotFoundException(String message){
        super(message);
    }
}