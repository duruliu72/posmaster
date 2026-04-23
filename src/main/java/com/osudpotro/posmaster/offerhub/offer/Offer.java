package com.osudpotro.posmaster.offerhub.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {
    private String name;
    private BigDecimal offerValue;
    private BigDecimal maxOfferValue;
    private LocalDateTime offerStartDate;
    private LocalDateTime offerEndDate;
    @JsonIgnore
    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<OfferTarget> offerTargets = new ArrayList<>();
}
