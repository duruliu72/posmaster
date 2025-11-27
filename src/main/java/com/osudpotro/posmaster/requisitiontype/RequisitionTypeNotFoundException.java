package com.osudpotro.posmaster.requisitiontype;

public class RequisitionTypeNotFoundException extends RuntimeException{
    public RequisitionTypeNotFoundException() {
        super("Generic not found");
    }

    public RequisitionTypeNotFoundException(String message) {
        super(message);
    }
}
