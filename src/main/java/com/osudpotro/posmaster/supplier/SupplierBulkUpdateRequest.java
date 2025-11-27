package com.osudpotro.posmaster.supplier;

import lombok.Data;

import java.util.List;

@Data
public class SupplierBulkUpdateRequest {
    private List<Long> supplierIds;
}
