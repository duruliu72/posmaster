package com.osudpotro.posmaster.purchase.purchasecart;

public class PurchaseCartItemNotFoundException extends RuntimeException{
    public PurchaseCartItemNotFoundException(){
        super("PurchaseCart Item not Found");
    }
    public PurchaseCartItemNotFoundException(String message){
        super(message);
    }
}
