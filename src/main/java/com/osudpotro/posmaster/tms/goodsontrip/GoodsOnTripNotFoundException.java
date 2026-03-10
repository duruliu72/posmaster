package com.osudpotro.posmaster.tms.goodsontrip;

public class GoodsOnTripNotFoundException extends RuntimeException{
    public GoodsOnTripNotFoundException() {
        super("Goods On Trip not found");
    }

    public GoodsOnTripNotFoundException(String message) {
        super(message);
    }
}
