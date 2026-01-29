package com.osudpotro.posmaster.purchase.requisition;

public class PurchaseRequisitionItemDuplicateException extends RuntimeException{
    public PurchaseRequisitionItemDuplicateException(){
        super("Purchase Requisition Item already added");
    }
    public PurchaseRequisitionItemDuplicateException(String message){
        super(message);
    }
}
