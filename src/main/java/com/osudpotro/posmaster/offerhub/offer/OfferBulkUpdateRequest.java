package com.osudpotro.posmaster.offerhub.offer;

import lombok.Data;

import java.util.List;

@Data
public class OfferBulkUpdateRequest {
    private List<Long> offerIds;
}
