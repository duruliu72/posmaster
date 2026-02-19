package com.osudpotro.posmaster.tms.vechile;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleFilter {
    private String name;
    private String licenceNo;
    private Integer status;
}
