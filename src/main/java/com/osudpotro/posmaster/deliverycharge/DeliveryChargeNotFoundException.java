package com.osudpotro.posmaster.deliverycharge;


public class DeliveryChargeNotFoundException extends RuntimeException {
    public DeliveryChargeNotFoundException() {
        super("Branch not found");
    }

    public DeliveryChargeNotFoundException(String message) {
        super(message);
    }
}