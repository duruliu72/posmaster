package com.osudpotro.posmaster.purchase.checked;

public class CheckedPurchaseRequisitionNotFoundException extends  RuntimeException{
    public CheckedPurchaseRequisitionNotFoundException(){
        super("Checked Purchase Requisition not Found");
    }
    public CheckedPurchaseRequisitionNotFoundException(String message){
        super(message);
    }
}
