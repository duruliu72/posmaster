package com.osudpotro.posmaster.offerhub.offer;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OfferUpdateRequest {
    private String name;
    private BigDecimal offerValue;
    private BigDecimal maxOfferValue;
    private String offerStartDate;
    private String offerEndDate;
}
