package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.user.UserPlainDto;
import com.osudpotro.posmaster.user.UserType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleDto {
    private Long id;
    private String saleRef;
    private String paymentMethod;
    private UserType userType = UserType.CUSTOMER;
    private Long customerUserId;
    private Long customerId;
    private String customerName;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerMobile;
    private Long organizationId;
    private String organizationName;
    private Long branchId;
    private String branchName;
    private Long warehouseId;
    private String warehouseName;
    private Boolean isStoreOut;
    private BigDecimal vat;
    private AmountType vatType;
    private Long billingAddressId;
    private String billingAddress;
    private Long deliveryAddressId;
    private String deliveryAddress;
    private Long deliveryMethodId;
    private BigDecimal deliveryFee;
    private BigDecimal minSaleAmountForDeliveryFree;
    private BigDecimal walletAmount;
    private String prescriptionDocs;
    private Long specialDiscountONId;
    private BigDecimal specialDiscount;
    private BigDecimal overallDiscount;
    private AmountType overallDiscountType;
    private BigDecimal adjustmentAmount;
    private Integer saleChannel;
    private Long saleStatusLogId;
    private Integer saleStatus;
    private Integer paymentStatus;
    private Integer saleType;

    private UserPlainDto salePointMan;
    private Long salePointManId;
    private String salePointManName;
    private String salePointEmail;
    private String salePointMobile;


    private UserPlainDto createdBy;
    private LocalDateTime createdAt;
    private List<SaleItemDto> items = new ArrayList<>();
    private List<SalePaymentDto> payments = new ArrayList<>();
    // Summary fields
    private Integer totalQty;
    private BigDecimal subTotalPrice;
    private BigDecimal grandTotalPrice;
    private BigDecimal cashReceiveAmount;
    private BigDecimal cashReturnAmount;
    // Add these fields to SaleDto.java
    private String specialInstruction;

    private String saleStatusLabel;
    private String paymentStatusLabel;
    private String customerAddress;


}