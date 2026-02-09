package com.osudpotro.posmaster.tms.vehicledriver;

import lombok.Data;

import java.util.List;

@Data
public class VehicleDriverBulkUpdateRequest {
    private List<Long> vehicleDriverIds;
}
