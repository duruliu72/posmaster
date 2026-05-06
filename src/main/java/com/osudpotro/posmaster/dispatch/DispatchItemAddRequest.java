package com.osudpotro.posmaster.dispatch;

import lombok.Data;

@Data
public class DispatchItemAddRequest {
    private Long productId;
    private Long productDetailId;
    private Integer qty;
}
