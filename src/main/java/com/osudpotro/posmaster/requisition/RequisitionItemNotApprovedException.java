package com.osudpotro.posmaster.requisition;

public class RequisitionItemNotApprovedException extends RuntimeException{
    public RequisitionItemNotApprovedException(){
        super("Requisition not Approved yet");
    }
    public RequisitionItemNotApprovedException(String message){
        super(message);
    }
}
