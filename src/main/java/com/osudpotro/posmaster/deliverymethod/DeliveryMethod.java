package com.osudpotro.posmaster.deliverymethod;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import jakarta.persistence.*;
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
@Table(name = "delivery_methods")
public class DeliveryMethod extends BaseEntity {
    private String title;
    private String message;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia media;
    private BigDecimal deliveryFee;
    private BigDecimal minSaleAmount;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_delivery_method_id", nullable = true)
    private DeliveryMethod parent;
    private Boolean isActive;
}
