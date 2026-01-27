package com.osudpotro.posmaster.requisition;

public class RequisitionUpdateException extends RuntimeException {
    public RequisitionUpdateException() {
        super("Requisition Update not Possible because of on Processing");
    }

    public RequisitionUpdateException(String message) {
        super(message);
    }
}
