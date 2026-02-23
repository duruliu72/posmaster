package com.osudpotro.posmaster.tms.vehicletrip;

import lombok.Data;

import java.util.List;

@Data
public class VehicleTripBulkUpdateRequest {
    private List<Long> vehicleTripIds;
}
