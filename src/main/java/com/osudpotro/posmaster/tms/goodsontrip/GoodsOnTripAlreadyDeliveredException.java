package com.osudpotro.posmaster.tms.goodsontrip;

public class GoodsOnTripAlreadyDeliveredException extends RuntimeException{
    public GoodsOnTripAlreadyDeliveredException() {
        super("Goods On Trip not found");
    }

    public GoodsOnTripAlreadyDeliveredException(String message) {
        super(message);
    }
}
