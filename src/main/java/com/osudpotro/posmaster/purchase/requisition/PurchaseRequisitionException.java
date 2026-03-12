package com.osudpotro.posmaster.purchase.requisition;

public class PurchaseRequisitionException extends RuntimeException {
    public PurchaseRequisitionException() {
        super("Duplicate Purchase Invoice Found");
    }

    public PurchaseRequisitionException(String message) {
        super(message);
    }
}
