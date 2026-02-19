package com.osudpotro.posmaster.tms.vechile;

public class DuplicateVehicleException extends RuntimeException{
    public DuplicateVehicleException() {
        super("Vehicle already exists");
    }

    public DuplicateVehicleException(String message) {
        super(message);
    }
}
