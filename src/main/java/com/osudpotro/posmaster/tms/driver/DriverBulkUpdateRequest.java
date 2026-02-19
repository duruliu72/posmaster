package com.osudpotro.posmaster.tms.driver;

import lombok.Data;

import java.util.List;

@Data
public class DriverBulkUpdateRequest {
    private List<Long> driverIds;
}
