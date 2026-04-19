package com.osudpotro.posmaster.address.district;

import lombok.Data;

import java.util.List;

@Data
public class DistrictBulkUpdateRequest {
    private List<Long> districtIds;
}
