package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequisitionInvoiceRefRequest {
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private BigDecimal overallDiscount;
    private Boolean isFinal;
}
