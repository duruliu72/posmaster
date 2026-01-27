package com.osudpotro.posmaster.purchase.requisition;

public class PurchaseRequisitionNotFoundException extends  RuntimeException{
    public PurchaseRequisitionNotFoundException(){
        super("Purchase Requisition not Found");
    }
    public PurchaseRequisitionNotFoundException(String message){
        super(message);
    }
}
