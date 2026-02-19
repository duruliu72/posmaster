package com.osudpotro.posmaster.tms.vechile;

import lombok.Data;

import java.util.List;

@Data
public class VehicleBulkUpdateRequest {
    private List<Long> vehicleIds;
}
