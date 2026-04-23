package com.osudpotro.posmaster.offerhub.offer;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class OfferDto {
    private Long id;
    private String name;
    private BigDecimal offerValue;
    private BigDecimal maxOfferValue;
    private LocalDateTime offerStartDate;
    private LocalDateTime offerEndDate;
    private List<OfferTarget> offerTargets = new ArrayList<>();
}
