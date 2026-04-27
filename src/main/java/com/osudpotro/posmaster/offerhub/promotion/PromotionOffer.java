package com.osudpotro.posmaster.offerhub.promotion;
import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "promotion_offers")
public class PromotionOffer extends BaseEntity {
  private String name;
  private String alias;
  @Column(name = "pro_desc")
  private String desc;
  private String promoCode;
  private BigDecimal promotionValue;
  private BigDecimal minOrderValue;
  private Integer maxCount;
  private LocalDateTime promoStartDate;
  private LocalDateTime promoEndDate;
}
