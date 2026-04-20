package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.address.area.AreaDto;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethodDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryChargeDto {
    private DeliveryMethodDto deliveryMethod;
    private BigDecimal deliveryFee;
    private BigDecimal minSaleAmountForDeliveryFree;
    private Integer chargeBasedOn;
    private Double minDistance;
    private AreaDto area;
//    private Long basedOnEntityId;
//    private String basedOnEntityName;
    private Boolean isFree;
    private Boolean isActive;
}
