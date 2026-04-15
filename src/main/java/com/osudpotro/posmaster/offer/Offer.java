package com.osudpotro.posmaster.offer;


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
@Table(name = "offers")
public class Offer extends BaseEntity {
    private String title;
    private BigDecimal offerValue;
    private LocalDateTime offerStartDate;
    private LocalDateTime offerEndDate;
}
