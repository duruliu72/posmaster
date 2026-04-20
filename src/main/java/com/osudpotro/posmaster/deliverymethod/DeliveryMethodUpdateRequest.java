package com.osudpotro.posmaster.deliverymethod;

import lombok.Data;

@Data
public class DeliveryMethodUpdateRequest {
    private String title;
    private String message;
    private String fromDate;
    private String toDate;
    private Long multimediaId;
}
