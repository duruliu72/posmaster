package com.osudpotro.posmaster.manufacturer;

import lombok.Data;

import java.util.List;

@Data
public class ManufacturerBulkUpdateRequest {
    private List<Long> manufacturerIds;
}
