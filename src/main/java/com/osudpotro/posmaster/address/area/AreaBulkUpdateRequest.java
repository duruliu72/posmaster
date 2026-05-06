package com.osudpotro.posmaster.address.area;

import lombok.Data;

import java.util.List;

@Data
public class AreaBulkUpdateRequest {
    private List<Long> areaIds;
}
