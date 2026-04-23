package com.osudpotro.posmaster.deliverymethod;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryMethodUpdateRequest {
    private String title;
    private String message;
    private String fromDate;
    private String toDate;
    private Long multimediaId;
    private BigDecimal defaultDeliveryFee;
    private BigDecimal defaultMinSaleAmountForDeliveryFree;
}
