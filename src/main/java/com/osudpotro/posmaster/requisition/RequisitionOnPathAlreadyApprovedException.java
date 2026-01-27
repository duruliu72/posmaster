package com.osudpotro.posmaster.requisition;

public class RequisitionOnPathAlreadyApprovedException extends RuntimeException{
    public RequisitionOnPathAlreadyApprovedException(){
        super("Approver on Path Already Approved");
    }
    public RequisitionOnPathAlreadyApprovedException(String message){
        super(message);
    }
}
