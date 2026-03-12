package com.osudpotro.posmaster.purchase.requisition;

public class PurchaseRequisitionItemException extends RuntimeException{
    public PurchaseRequisitionItemException(){
        super("Purchase Requisition Item already added");
    }
    public PurchaseRequisitionItemException(String message){
        super(message);
    }
}
