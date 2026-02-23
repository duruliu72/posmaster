package com.osudpotro.posmaster.tms.vehicletrip;

public class DuplicateVehicleTripException extends  RuntimeException{
    public DuplicateVehicleTripException() {
        super("Vehicle Trip already exists");
    }

    public DuplicateVehicleTripException(String message) {
        super(message);
    }
}
