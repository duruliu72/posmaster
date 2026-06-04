package com.osudpotro.posmaster.sale;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PosSaleCreateRequest {
    private Long customerId;// get membership;
    private BigDecimal overallDiscount;
    private AmountType overallDiscountType;
    private BigDecimal vat;
    private AmountType vatType;
    private BigDecimal adjustmentAmount;
    private String prescriptionDocs;
    private String specialInstruction;
    private Long promotionOfferId;

    //    Sale Payment info
    private List<SalePaymentRequest> salePayments = new ArrayList<>();
    private BigDecimal cashReturnAmount;
    private List<SaleItemRequest> items = new ArrayList<>();
}
