package com.osudpotro.posmaster.tms.goodsonvechile;

import lombok.Data;

@Data
public class GoodsOnTripRequest {
    private String sourceAddress;
    private Double sourceLatitude;
    private Double sourceLongitude;
    private Double sourceAccuracy;
    private String destAddress;
    private Double destLatitude;
    private Double destLongitude;
    private Double destAccuracy;
}
