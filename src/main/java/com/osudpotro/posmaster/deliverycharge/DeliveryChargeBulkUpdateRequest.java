package com.osudpotro.posmaster.deliverycharge;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryChargeBulkUpdateRequest {
    private List<Long> deliveryChargeIds;
}
