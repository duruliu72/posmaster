package com.osudpotro.posmaster.tms.goodsonvechile;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class GoodsOnTripDto{
    private Long id;
    private GoodsType goodsType;
    private String goodsReference;
    private String goodsReferenceDocs;
    private GoodsStatus goodsStatus;
    private String receivedBy;
    private String signaturePath;
    private String remarks;
    private LocalDateTime loadedAt;
    private LocalDateTime unloadedAt;
    private String loadedBy;
    private String unloadedBy;
}
