package com.osudpotro.posmaster.tms.vehicledriver;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleDriverFilter {
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Integer status;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
}
