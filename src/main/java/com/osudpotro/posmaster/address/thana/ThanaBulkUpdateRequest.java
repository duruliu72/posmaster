package com.osudpotro.posmaster.address.thana;

import lombok.Data;

import java.util.List;

@Data
public class ThanaBulkUpdateRequest {
    private List<Long> thanaIds;
}
