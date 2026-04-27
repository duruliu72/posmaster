package com.osudpotro.posmaster.offerhub.promotion;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PromotionOfferDto{
  private Long id;
  private String name;
  private String alias;
  private String desc;
  private String promoCode;
  private BigDecimal promotionValue;
  private BigDecimal minOrderValue;
  private Integer maxCount;
  private LocalDateTime promoStartDate;
  private LocalDateTime promoEndDate;
}
