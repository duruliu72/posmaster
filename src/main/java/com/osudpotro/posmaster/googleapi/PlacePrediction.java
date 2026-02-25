package com.osudpotro.posmaster.googleapi;

import com.osudpotro.posmaster.common.Location;
import lombok.Data;

@Data
public class PlacePrediction {
    private String description;
    private String placeId;
    private String mainText;
    private Double latitude;
    private Double longitude;
    private Location northeast;
    private Location southwest;
//    private Double accuracy;
}
