package com.osudpotro.posmaster.tms.goodsontrip;

public class GoodsOnTripDeliveryException extends RuntimeException{
    public GoodsOnTripDeliveryException() {
        super("Goods On Trip not found");
    }

    public GoodsOnTripDeliveryException(String message) {
        super(message);
    }
}
