package com.osudpotro.posmaster.deliverymethod;


public class DeliveryMethodNotFoundException extends RuntimeException {
    public DeliveryMethodNotFoundException() {
        super("Branch not found");
    }

    public DeliveryMethodNotFoundException(String message) {
        super(message);
    }
}