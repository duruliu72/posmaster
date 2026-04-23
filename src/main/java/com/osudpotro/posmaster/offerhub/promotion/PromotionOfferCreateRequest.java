package com.osudpotro.posmaster.offerhub.promotion;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PromotionOfferCreateRequest {
    private String name;
    private String alias;
    private String desc;
    private String promoCode;
    private BigDecimal promotionValue;
    private BigDecimal minOrderValue;
    private Integer maxCount;
    private String promoStartDate;
    private String promoEndDate;
}
