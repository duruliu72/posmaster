package com.osudpotro.posmaster.purchase.transfer;

import lombok.Data;

@Data
public class AssignToVehicleRequest {
    private String tripRef;
    private Long driverId;
    private Long vehicleId;
    private String sourceAddress;
    private  String destAddress;
}
