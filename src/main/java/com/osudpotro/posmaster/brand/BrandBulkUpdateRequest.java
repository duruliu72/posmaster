package com.osudpotro.posmaster.brand;

import lombok.Data;

import java.util.List;

@Data
public class BrandBulkUpdateRequest {
    private List<Long> brandIds;
}
