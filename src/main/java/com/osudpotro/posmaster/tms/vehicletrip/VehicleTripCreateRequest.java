package com.osudpotro.posmaster.tms.vehicletrip;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleTripCreateRequest {
    private String tripRef;
    private Long vehicleId;
    private Long driverId;
    private LocalDateTime tripStartTime;
    private LocalDateTime tripEndTime;
}
