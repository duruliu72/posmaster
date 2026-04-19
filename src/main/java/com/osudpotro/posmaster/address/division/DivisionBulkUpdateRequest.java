package com.osudpotro.posmaster.address.division;

import lombok.Data;

import java.util.List;

@Data
public class DivisionBulkUpdateRequest {
    private List<Long> divisionIds;
}
