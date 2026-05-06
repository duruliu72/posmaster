package com.osudpotro.posmaster.deliverymethod;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryMethodBulkUpdateRequest {
    private List<Long> deliveryMethodIds;
}
