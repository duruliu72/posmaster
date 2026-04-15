package com.osudpotro.posmaster.promotion;
import com.osudpotro.posmaster.common.BaseEntity;
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
  private String title;
  private BigDecimal promotionValue;
  private LocalDateTime promoStartDate;
  private LocalDateTime promoEndDate;
}
