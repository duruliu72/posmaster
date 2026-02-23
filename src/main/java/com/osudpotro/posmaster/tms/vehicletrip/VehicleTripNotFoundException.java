package com.osudpotro.posmaster.tms.vehicletrip;

public class VehicleTripNotFoundException extends RuntimeException{
    public VehicleTripNotFoundException() {
        super("Vehicle Trip not found");
    }

    public VehicleTripNotFoundException(String message) {
        super(message);
    }
}
