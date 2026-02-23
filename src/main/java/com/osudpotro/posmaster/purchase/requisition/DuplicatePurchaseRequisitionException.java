package com.osudpotro.posmaster.purchase.requisition;

public class DuplicatePurchaseRequisitionException extends RuntimeException {
    public DuplicatePurchaseRequisitionException() {
        super("Duplicate Purchase Invoice Found");
    }

    public DuplicatePurchaseRequisitionException(String message) {
        super(message);
    }
}
