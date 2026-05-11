package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleDto {
    private Long id;
    private String saleRef;
    private OrganizationDto organization;
    private BranchDto branch;
    private BigDecimal vatAmount;
    private String billingAddress;
    private String deliveryAddress;
    private BigDecimal deliveryFee;
    private String prescriptionDocs;
    private Integer saleChannel;
    private Integer saleStatus;
    private Integer paymentStatus;
    private Integer saleType;
    private UserPlainDto customer;
    private UserPlainDto salePointMan;
    private UserPlainDto createdBy;
    private LocalDateTime createdAt;
    private List<SaleItemDto> items = new ArrayList<>();
    private List<SalePaymentDto> payments = new ArrayList<>();
    // Summary fields
    private Integer totalQty;
    private BigDecimal totalPrice;

    // Add these fields to SaleDto.java
    private String customerName;
    private String customerMobile;
    private String paymentMethod;
    private String specialInstruction;
    private String deliveryMethod;
}