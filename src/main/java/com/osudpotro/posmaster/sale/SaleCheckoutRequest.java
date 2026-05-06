package com.osudpotro.posmaster.sale;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleCheckoutRequest {
    // Customer Info
    private String email;
    private String mobile;
    private String customerName;

    // Address
    private String specialInstruction;
    private String address;
    private String houseOrFlatNo;

    // Delivery
    private Long deliveryMethodId;
    private String offerStartDate;
    private String offerEndDate;
    private Boolean isMerchant;

    // Payment
    private String paymentMethod; // cod, bkash, nagad, etc.
    private String trxId;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;

    // Sale Settings
    private Integer saleStatus;
    private Integer paymentStatus;
    private Integer saleType;
    private Integer saleChannel;
    private BigDecimal deliveryFee;
    private String prescriptionDocs;
    private BigDecimal vatAmount;
}