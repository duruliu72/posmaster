package com.osudpotro.posmaster.tms.vehicletrip;

import lombok.Data;

@Data
public class UpdateVehicleTripRequest {
    private String tripRef;
    private String tripStatus;
}
