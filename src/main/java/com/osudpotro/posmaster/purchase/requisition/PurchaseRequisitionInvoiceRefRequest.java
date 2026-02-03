package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

@Data
public class PurchaseRequisitionInvoiceRefRequest {
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private Boolean isFinal;
}
