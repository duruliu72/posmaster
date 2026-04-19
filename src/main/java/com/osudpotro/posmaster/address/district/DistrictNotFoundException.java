package com.osudpotro.posmaster.address.district;


public class DistrictNotFoundException extends RuntimeException {
    public DistrictNotFoundException() {
        super("Branch not found");
    }

    public DistrictNotFoundException(String message) {
        super(message);
    }
}