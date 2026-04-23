package com.osudpotro.posmaster.deliverycharge;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryChargeUpdateRequest {
    private Long deliveryMethodId;
    private BigDecimal deliveryFee;
    private BigDecimal minSaleAmountForDeliveryFree;
    private Integer chargeBasedOn;
    private Double minDistance;
    private Long areaId;
    private Boolean isFree;
}
