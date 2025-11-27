package com.osudpotro.posmaster.varianttype;

import lombok.Data;

import java.util.List;

@Data
public class VariantTypeBulkUpdateRequest {
    private List<Long>  variantTypeIds;
}
