package com.osudpotro.posmaster.tms.vehicledriver;

public class DuplicateVehicleDriverException extends RuntimeException{
    public DuplicateVehicleDriverException() {
        super("Driver already exists");
    }

    public DuplicateVehicleDriverException(String message) {
        super(message);
    }
}
