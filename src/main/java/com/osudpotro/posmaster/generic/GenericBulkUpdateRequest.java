package com.osudpotro.posmaster.generic;

import lombok.Data;

import java.util.List;

@Data
public class GenericBulkUpdateRequest {
    private List<Long> genericIds;
}
