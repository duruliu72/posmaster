package com.osudpotro.posmaster.genericunit;

import lombok.Data;

import java.util.List;

@Data
public class GenericUnitBulkUpdateRequest {
    private List<Long> genericUnitIds;
}
