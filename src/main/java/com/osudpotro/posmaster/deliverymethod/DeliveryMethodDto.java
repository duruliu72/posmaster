package com.osudpotro.posmaster.deliverymethod;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryMethodDto {
    private String title;
    private String message;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
