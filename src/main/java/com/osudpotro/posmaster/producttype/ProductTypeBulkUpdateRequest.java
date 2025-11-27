package com.osudpotro.posmaster.producttype;

import lombok.Data;

import java.util.List;

@Data
public class ProductTypeBulkUpdateRequest {
    private List<Long> productTypeIds;
}
