package com.osudpotro.posmaster.purchase.purchasecart;

public class PurchaseCartNotFoundException extends RuntimeException{
    public PurchaseCartNotFoundException(){
        super("PurchaseCart not Found");
    }
    public PurchaseCartNotFoundException(String message){
        super(message);
    }
}
