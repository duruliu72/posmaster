package com.osudpotro.posmaster.purchase.requisition;

public class PurchaseRequisitionEmptyException extends RuntimeException{
    public PurchaseRequisitionEmptyException(){
        super("Purchase Requisition Empty Please Added first");
    }
    public PurchaseRequisitionEmptyException(String message){
        super(message);
    }
}
