package com.osudpotro.posmaster.tms.goodsontrip;

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
