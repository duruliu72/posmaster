package com.osudpotro.posmaster.offerhub.promotion;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionOfferUpdateRequest {
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
