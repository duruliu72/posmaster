package com.osudpotro.posmaster.tms.vehicledriver;

public class VehicleDriverNotFoundException extends RuntimeException{
    public VehicleDriverNotFoundException() {
        super("Driver not found");
    }

    public VehicleDriverNotFoundException(String message) {
        super(message);
    }
}
